package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.api.dto.user.UserDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentService;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectService;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticService;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    private final CommentService commentService;
    private final ProjectService projectService;
    private final StatisticService statisticService;
    private final RoleService roleService;

    public UserConverter(CommentService commentService, ProjectService projectService, StatisticService statisticService, RoleService roleService) {
        this.commentService = commentService;
        this.projectService = projectService;
        this.statisticService = statisticService;
        this.roleService = roleService;
    }

    public User toModel(UserDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getPathToFileWithPhoto(),
                userDTO.getCommentsIDs().stream().map(commentService::getCommentById).collect(Collectors.toList()),
                userDTO.getProjectsIDs().stream().map(projectService::getProjectById).collect(Collectors.toList()),
                userDTO.getStatisticsIDs().stream().map(statisticServicee::getStatisticById).collect(Collectors.toList()),
                userDTO.getRolesIDs().stream().map(roleService::getRolesById).collect(Collectors.toList())
                );
    }

    public UserDTO fromModel(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPathToFileWithPhoto(),
                user.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList()),
                user.getProjects().stream().map(Project::getProjectId).collect(Collectors.toList()),
                user.getStatistics().stream().map(Statistic::getStatisticId).collect(Collectors.toList()),
                user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
                );
    }

    public Collection<User> toModelsMany(Collection<UserDTO> userDTOS) {
        return userDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<UserDTO> fromModelsMany(Collection<User> users) {
        return users.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
