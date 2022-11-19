package cz.cvut.fit.sp1.githubreports.dao.project;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> readAllByCommit(Commit commit, Pageable pageable);
}
