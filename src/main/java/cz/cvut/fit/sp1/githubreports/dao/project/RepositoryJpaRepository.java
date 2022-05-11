package cz.cvut.fit.sp1.githubreports.dao.project;

import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface RepositoryJpaRepository extends JpaRepository<Repository, Long> {
    Optional<Repository> findRepositoryByRepositoryName(String repositoryName);
}
