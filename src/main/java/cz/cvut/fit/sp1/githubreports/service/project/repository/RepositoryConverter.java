package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitService;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class RepositoryConverter {

    private CommitService commitService;
    private ProjectService projectService;

    public Repository toModel(RepositoryDTO repositoryDTO) {
        return new Repository(repositoryDTO.getRepositoryID(), repositoryDTO.getRepositoryName(),
                repositoryDTO.getCommitsIDs().stream().map(commitID -> commitSPI.readById(commitID).orElseThrow(IncorrectRequestException::new)).collect(Collectors.toList()),
                repositoryDTO.getProjectsIDs().stream().map(projectID -> projectSPI.readById(projectID).orElseThrow(IncorrectRequestException::new)).collect(Collectors.toList()));
    }

    public RepositoryDTO fromModel(Repository repository) {
        return new RepositoryDTO(repository.getRepositoryId(), repository.getRepositoryName(),
                repository.getCommits().stream().map(Commit::getCommitId).collect(Collectors.toList()),
                repository.getProjects().stream().map(Project::getProjectId).collect(Collectors.toList()));
    }

    public Collection<Repository> toModelsMany(Collection<RepositoryDTO> repositoryDTOs) {
        return repositoryDTOs.stream().map(this::toModel).toList();
    }

    public Collection<RepositoryDTO> fromModelsMany(Collection<Repository> repositories) {
        return repositories.stream().map(this::fromModel).toList();
    }
}
