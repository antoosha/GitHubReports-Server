package cz.cvut.fit.sp1.githubreports.dao.project;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
