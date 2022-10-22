package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.CommentJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.CommitJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.TagJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentMapper;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.model.CommentUpdateSlimDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Transactional
@Service("CommitService")
public class CommitService implements CommitSPI {

    private static final Logger logger = Logger.getLogger(CommitService.class.getName());

    private final CommitJpaRepository commitJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final TagJpaRepository tagJpaRepository;

    private final CommentMapper commentMapper;

    private final CommentSPI commentSPI;

    private final UserSPI userSPI;

    private void checkValidation(Commit commit) {
        if (commit.getRepository() == null){
            logger.warning("Commit does not have repository.");
            throw new EntityStateException("Commit does not have repository.");
        }
        if (commit.getLoginAuthor().isEmpty()){
            logger.warning("Commit does not have author login.");
            throw new EntityStateException("Commit does not have author login.");
        }
    }

    @Override
    public Collection<Commit> readAll() {
        return commitJpaRepository.findAll();
    }

    @Override
    public Commit readById(Long id) {
        return commitJpaRepository.findById(id)
                .orElseThrow( () -> {
                    logger.warning("Commit with id " + id + " does not exist.");
                    return new NoEntityFoundException("Commit with id " + id + " does not exist.");
                });
    }

    @Override
    public Commit create(Commit commit) throws EntityStateException {
        if (commit.getCommitId() != null) {
            if (commitJpaRepository.existsById(commit.getCommitId())){
                logger.warning("Commit with id " + commit.getCommitId() + " already exists.");
                throw new EntityStateException("Commit with id " + commit.getCommitId() + " already exists.");
            }
        }
        checkValidation(commit);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit update(Long id, Commit commit) throws EntityStateException {
        if (id == null || !commitJpaRepository.existsById(id)){
            logger.warning("Commit with id " + id + " does not exist.");
            throw new NoEntityFoundException("Commit with id" + id + " does not exist.");
        }
        checkValidation(commit);
        return commitJpaRepository.save(commit);
    }

    @Override
    public void delete(Long id) {
        if (commitJpaRepository.existsById(id))
            commitJpaRepository.deleteById(id);
        else {
            logger.warning("Commit with id " + id + " does not exist.");
            throw new NoEntityFoundException("Commit with id" + id + " does not exist.");
        }
    }

    @Override
    public void changeIsDeleted(Commit commit, boolean isDeleted) {
        commit.setDeleted(isDeleted);
        commitJpaRepository.save(commit);
    }

    @Override
    public Commit addComment(Long id, CommentUpdateSlimDTO commentUpdateSlimDTO) {
        Commit commit = commitJpaRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Commit with id " + id + " does not exist.");
                    return new NoEntityFoundException("Commit with id" + id + " does not exist.");
                });
        Comment comment = commentMapper.fromUpdateSlimDTO(commentUpdateSlimDTO);
        comment.setAuthor(userSPI.readUserFromToken());
        comment.setCommit(commit);
        commentSPI.create(comment);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit addTag(Long id, Long tagId) {
        Commit commit = commitJpaRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Commit with id " + id + " does not exist.");
                    return new NoEntityFoundException("Commit with id" + id + " does not exist.");
                });
        Tag tag = tagJpaRepository
                .findById(tagId)
                .orElseThrow(() -> {
                    logger.warning("Tag with id" + id + " does not exist.");
                    return new NoEntityFoundException("Tag with id" + id + " does not exist.");
                });
        if (commit.getTags().contains(tag)) {
            logger.warning("Commit with id " + id + " already have tag with id " + tagId);
            throw new IncorrectRequestException("Commit with id " + id + " already have tag with id " + tagId);
        }
        commit.getTags().add(tag);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit deleteComment(Long id, Long commentId) {
        Commit commit = commitJpaRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Commit with id" + id + " does not exist.");
                    return new NoEntityFoundException("Commit with id" + id + " does not exist.");
                });
        Comment comment = commentJpaRepository
                .findById(commentId)
                .orElseThrow(() -> {
                    logger.warning("Comment with id" + commentId + " does not exist.");
                    return new NoEntityFoundException("Comment with id" + commentId + " does not exist.");
                });
        if (!commit.getComments().contains(comment)) {
            logger.warning("Commit with id " + commentId + " does not have comment with id " + commentId);
            throw new IncorrectRequestException
                    ("Commit with id " + commentId + " does not have comment with id " + commentId);
        }
        commit.getComments().remove(comment);
        return commitJpaRepository.save(commit);
    }

    @Override
    public Commit removeTag(Long id, Long tagId) {
        Commit commit = commitJpaRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Commit with id " + id + " does not exist.");
                    return new NoEntityFoundException("Commit with id" + id + " does not exist.");
                });
        Tag tag = tagJpaRepository
                .findById(tagId)
                .orElseThrow(() -> {
                    logger.warning("Tag with id" + tagId + " does not exist.");
                    return new NoEntityFoundException("Tag with id" + tagId + " does not exist.");
                });
        if (!commit.getTags().contains(tag)) {
            logger.warning("Commit with id " + id + " does not have tag with id " + tagId);
            throw new IncorrectRequestException
                    ("Commit with id " + id + " does not have tag with id " + tagId);
        }
        commit.getTags().remove(tag);
        return commitJpaRepository.save(commit);
    }
}
