package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.api.dto.user.UserUpdateDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticSPI;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserUpdateConverter {
    private final CommentSPI commentSPI;
    private final ProjectSPI projectSPI;
    private final StatisticSPI statisticSPI;
    private final RoleSPI roleSPI;

    public User toModel(UserUpdateDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                null,
                userDTO.getPathToFileWithPhoto(),
                userDTO.getCommentsIDs().stream()
                        .map(commentID -> commentSPI.readById(commentID).orElseThrow(IncorrectRequestException::new))
                        .collect(Collectors.toList()),
                userDTO.getProjectsIDs().stream()
                        .map(projectID -> projectSPI.readById(projectID).orElseThrow(IncorrectRequestException::new))
                        .collect(Collectors.toList()),
                userDTO.getCreatedProjectsIDs().stream()
                        .map(createdProjectID -> projectSPI.readById(createdProjectID).orElseThrow(IncorrectRequestException::new))
                        .collect(Collectors.toList()),
                userDTO.getStatisticsIDs().stream()
                        .map(statisticID -> statisticSPI.readById(statisticID).orElseThrow(IncorrectRequestException::new))
                        .collect(Collectors.toList()),
                userDTO.getRolesIDs().stream()
                        .map(roleID -> roleSPI.readById(roleID).orElseThrow(IncorrectRequestException::new))
                        .collect(Collectors.toList())
        );
    }

    public UserUpdateDTO fromModel(User user) {
        return new UserUpdateDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPathToFileWithPhoto(),
                user.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList()),
                user.getProjects().stream().map(Project::getProjectId).collect(Collectors.toList()),
                user.getCreatedProjects().stream().map(Project::getProjectId).collect(Collectors.toList()),
                user.getStatistics().stream().map(Statistic::getStatisticId).collect(Collectors.toList()),
                user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
        );
    }

    public Collection<User> toModelsMany(Collection<UserUpdateDTO> userDTOS) {
        return userDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<UserUpdateDTO> fromModelsMany(Collection<User> users) {
        return users.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
