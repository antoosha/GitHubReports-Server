package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectMapper;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryMapper;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagMapper;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapi.api.ProjectsApi;
import org.openapi.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProjectApiDelegateImpl implements ProjectsApi {
    private final ProjectSPI projectSPI;
    private final ProjectMapper projectMapper;
    private final RepositoryMapper repositoryMapper;
    private final StatisticMapper statisticMapper;
    private final TagMapper tagMapper;

    @Override
    public ResponseEntity<ProjectDTO> createProject(ProjectUpdateSlimDTO projectUpdateSlimDTO) {
        return ResponseEntity.ok(
                projectMapper.toDTO(
                        projectSPI.create(projectMapper.fromUpdateSlimDTO(projectUpdateSlimDTO))));
    }

    @Override
    public ResponseEntity<RepositoryDTO> createRepository(Long id, String githubToken, RepositorySlimDTO repositorySlimDTO) {
        return ResponseEntity.ok(
                repositoryMapper.toDTO(
                        projectSPI.createRepository(id, githubToken, repositoryMapper.fromSlimDTO(repositorySlimDTO))));
    }

//    @Override
//    public ResponseEntity<StatisticDTO> createStatistic(Long id, String statisticType) {
//        return ResponseEntity.ok(
//                statisticMapper.toDTO(
//                projectSPI.createStatistic(id, statisticType))); //TODO
//    }

    @Override
    public ResponseEntity<TagDTO> createTag(Long id, TagSlimDTO tagSlimDTO) {
        return ResponseEntity.ok(
                tagMapper.toDTO(
                        projectSPI.createTag(id, tagMapper.fromSlimDTO(tagSlimDTO))));
    }

    @Override
    public ResponseEntity<Void> deleteProject(Long id) {
        projectSPI.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectDTO> getProject(Long id) {
        return ResponseEntity.ok(projectMapper.toDTO(projectSPI.readById(id)));
    }

    @Override
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(projectMapper.toDTOs(projectSPI.readAll().stream().toList()));
    }

    @Override
    public ResponseEntity<ProjectDTO> updateProject(Long id, ProjectUpdateSlimDTO projectUpdateSlimDTO) {
        return ResponseEntity.ok(
                projectMapper.toDTO(
                        projectSPI.update(id, projectMapper.fromUpdateSlimDTO(projectUpdateSlimDTO))));
    }
}
