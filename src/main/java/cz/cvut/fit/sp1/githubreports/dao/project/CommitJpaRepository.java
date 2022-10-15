package cz.cvut.fit.sp1.githubreports.dao.project;

import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.openapi.model.CommitSlimDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitJpaRepository extends JpaRepository<Commit, Long> {
    List<Commit> findAllByRepository(cz.cvut.fit.sp1.githubreports.model.project.Repository repository, Pageable pageable);
}
