package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryMapper;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import lombok.AllArgsConstructor;
import org.openapi.api.RepositoriesApi;
import org.openapi.model.CommitSlimDTO;
import org.openapi.model.RepositoryDTO;
import org.openapi.model.RepositoryUpdateSlimDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class RepositoryApiDelegateImpl implements RepositoriesApi {

    private final RepositorySPI repositorySPI;
    private final RepositoryMapper repositoryMapper;

    @Override
    public ResponseEntity<Void> deleteRepository(Long id) {
        repositorySPI.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RepositoryDTO> getRepository(Long id) {
        return ResponseEntity.ok(
                repositoryMapper.toDTO(
                        repositorySPI.readById(id)));
    }

    @Override
    public ResponseEntity<RepositoryDTO> synchronize(Long id, String tokenAuth) {
        return ResponseEntity.ok(
                repositoryMapper.toDTO(
                        repositorySPI.synchronize(id, tokenAuth)));
    }


    @Override
    public ResponseEntity<RepositoryDTO> updateRepository(Long id, RepositoryUpdateSlimDTO repositoryUpdateSlimDTO) {
        return ResponseEntity.ok(
                repositoryMapper.toDTO(
                        repositorySPI.update(id, repositoryMapper.fromUpdateSlimDTO(repositoryUpdateSlimDTO))));
    }

    @Override
    public ResponseEntity<List<CommitSlimDTO>> getRepositoryCommits(Long id, Integer page, Integer size) {
        return ResponseEntity.ok(repositorySPI.readCommitsByRepositoryId(id, page, size));
    }
}
