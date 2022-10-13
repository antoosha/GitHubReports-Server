package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.CommentJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.CommitJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.TagJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentMapper;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentService;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagMapper;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.openapi.model.CommentUpdateSlimDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service("CommitService")
public class CommitService implements CommitSPI {

    private final CommitJpaRepository commitJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final TagJpaRepository tagJpaRepository;

    private final CommentMapper commentMapper;

    private final CommentService commentService;

    private final UserJpaRepository userJpaRepository;


    private void checkValidation(Commit commit) {
        if (commit.getRepository() == null)
            throw new EntityStateException();
        if (commit.getLoginAuthor().isEmpty()) throw new EntityStateException();
    }

    @Override
    public Collection<Commit> readAll() {
        return commitJpaRepository.findAll();
    }

    @Override
    public Optional<Commit> readById(Long id) {
        return commitJpaRepository.findById(id);
    }

    @Override
    public Commit create(Commit commit) throws EntityStateException {
        if (commit.getCommitId() != null) {
            if (commitJpaRepository.existsById(commit.getCommitId()))
                throw new EntityStateException();
        }
        checkValidation(commit);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit update(Long id, Commit commit) throws EntityStateException {
        if (id == null || !commitJpaRepository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(commit);
        return commitJpaRepository.save(commit);
    }

    @Override
    public void delete(Long id) {
        if (commitJpaRepository.existsById(id))
            commitJpaRepository.deleteById(id);
        else throw new NoEntityFoundException();
    }

    @Override
    public void changeIsDeleted(Commit commit, boolean isDeleted) {
        commit.setDeleted(isDeleted);
        commitJpaRepository.save(commit);
    }

    @Override
    public Commit addComment(Long id, CommentUpdateSlimDTO commentUpdateSlimDTO) {
        Commit commit = commitJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
        Comment comment = commentMapper.fromUpdateSlimDTO(commentUpdateSlimDTO);
        comment.setAuthor(userJpaRepository.findUserByUsername(commit.getLoginAuthor()).orElseThrow(NoEntityFoundException::new));
        commentService.create(comment);
        commit.getComments().add(comment);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit addTag(Long id, Long tagId) {
        Commit commit = commitJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
        Tag tag = tagJpaRepository.findById(tagId).orElseThrow(NoEntityFoundException::new);
        if (commit.getTags().contains(tag)) {
            throw new IncorrectRequestException();
        }
        commit.getTags().add(tag);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit deleteComment(Long id, Long commentId) {
        Commit commit = commitJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
        Comment comment = commentJpaRepository.findById(commentId).orElseThrow(NoEntityFoundException::new);
        if (!commit.getComments().contains(comment)) {
            throw new IncorrectRequestException();
        }
        commit.getComments().remove(comment);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit removeTag(Long id, Long tagId) {
        Commit commit = commitJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
        Tag tag = tagJpaRepository.findById(tagId).orElseThrow(NoEntityFoundException::new);
        if (!commit.getTags().contains(tag)) {
            throw new IncorrectRequestException();
        }
        commit.getTags().remove(tag);
        return commitJpaRepository.save(commit);
    }
}
