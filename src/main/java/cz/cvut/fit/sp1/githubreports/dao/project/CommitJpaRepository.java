package cz.cvut.fit.sp1.githubreports.dao.project;

import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitJpaRepository extends JpaRepository<Commit, Long> {
}
