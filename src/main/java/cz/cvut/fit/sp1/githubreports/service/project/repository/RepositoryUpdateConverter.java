package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryUpdateDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Component
public class RepositoryUpdateConverter {

    private CommitSPI commitSPI;
    private ProjectSPI projectSPI;

    public Repository toModel(RepositoryUpdateDTO repositoryUpdateDTO) {
        return new Repository(repositoryUpdateDTO.getRepositoryID(), repositoryUpdateDTO.getRepositoryName(),
                repositoryUpdateDTO.getRepositoryURL(),
                projectSPI.readById(repositoryUpdateDTO.getProjectID()).orElseThrow(IncorrectRequestException::new),
                new ArrayList<>()
                );
    }

    public RepositoryUpdateDTO fromModel(Repository repository) {
        return new RepositoryUpdateDTO(repository.getRepositoryId(), repository.getRepositoryName(),
                repository.getRepositoryURL(),
                repository.getProject().getProjectId());
    }

    public Collection<Repository> toModelsMany(Collection<RepositoryUpdateDTO> repositoryUpdateDTOS) {
        return repositoryUpdateDTOS.stream().map(this::toModel).toList();
    }

    public Collection<RepositoryUpdateDTO> fromModelsMany(Collection<Repository> repositories) {
        return repositories.stream().map(this::fromModel).toList();
    }
}
