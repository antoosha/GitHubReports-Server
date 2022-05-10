package cz.cvut.fit.sp1.githubreports;

import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleService;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class GithubReportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubReportsApplication.class, args);
	}

	/*@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.create(new Role("ROLE_ADMIN", Collections.emptyList()));
			roleService.create(new Role("ROLE_DEVELOPER", Collections.emptyList()));

			//DO NOT CHANGE USERNAME of deletedUser! HAS RELATIONS IN UserService by username!
			userService.create(
					new User(
							null,
							"deletedUser",
							"mailDeletedUser",
							"pass",
							"photoDeletedUser",
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList()));
			userService.create(
					new User(
							null,
							"user",
							"mail1",
							"pass",
							"photo",
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							List.of(roleService.readById("ROLE_ADMIN").get(), roleService.readById("ROLE_DEVELOPER").get())));
			userService.create(
					new User(
							null,
							"admin",
							"mail2",
							"pass",
							"photo",
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							List.of(roleService.readById("ROLE_ADMIN").get())));
			userService.create(
					new User(
							null,
							"dev",
							"mail3",
							"pass",
							"photo",
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							Collections.emptyList(),
							List.of(roleService.readById("ROLE_DEVELOPER").get())));
		};
	}*/

}
