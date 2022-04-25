package cz.cvut.fit.sp1.githubreports.service.project.commit;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;

import java.util.Collection;
import java.util.Optional;

public interface CommitSPI {

    Collection<Commit> readAll();

    Optional<Commit> readById(Long id);

    void create(Commit commit);

    void update(Long id, Commit commit);

    void delete(Long id);
}
