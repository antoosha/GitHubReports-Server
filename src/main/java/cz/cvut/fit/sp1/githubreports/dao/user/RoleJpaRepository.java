package cz.cvut.fit.sp1.githubreports.dao.user;

import cz.cvut.fit.sp1.githubreports.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, String> {
}
