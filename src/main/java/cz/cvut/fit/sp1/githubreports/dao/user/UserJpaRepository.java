package cz.cvut.fit.sp1.githubreports.dao.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {
}
