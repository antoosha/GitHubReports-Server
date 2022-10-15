package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitMapper;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.api.CommitsApi;
import org.openapi.model.CommentUpdateSlimDTO;
import org.openapi.model.CommitDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommitApiDelegateImpl implements CommitsApi {

    private final CommitSPI commitSPI;

    private final CommitMapper commitMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return CommitsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<CommitDTO> addComment(Long id, CommentUpdateSlimDTO commentUpdateSlimDTO) {
        CommitDTO commitDTO = commitMapper.toDTO(commitSPI.addComment(id, commentUpdateSlimDTO));
        return ResponseEntity.ok(commitDTO);
    }

    @Override
    public ResponseEntity<CommitDTO> addTag(Long id, Long tagId) {
        CommitDTO commitDTO = commitMapper.toDTO(commitSPI.addTag(id, tagId));
        return ResponseEntity.ok(commitDTO);
    }

    @Override
    public ResponseEntity<CommitDTO> deleteComment(Long id, Long commentId) {
        CommitDTO commitDTO = commitMapper.toDTO(commitSPI.deleteComment(id, commentId));
        return ResponseEntity.ok(commitDTO);
    }

    @Override
    public ResponseEntity<CommitDTO> getCommit(Long id) {
        CommitDTO commitDTO = commitMapper.toDTO(commitSPI.readById(id));
        return ResponseEntity.ok(commitDTO);
    }

    @Override
    public ResponseEntity<CommitDTO> removeTag(Long id, Long tagId) {
        CommitDTO commitDTO = commitMapper.toDTO(commitSPI.removeTag(id, tagId));
        return ResponseEntity.ok(commitDTO);
    }
}
