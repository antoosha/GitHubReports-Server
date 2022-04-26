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

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.create(new Role("ROLE_ADMIN", Collections.emptyList()));
			roleService.create(new Role("ROLE_DEVELOPER", Collections.emptyList()));

			userService.create(
					new User(
							null,
							"user",
							"mail",
							"pass",
							"photo",
							List.of(roleService.readById("ROLE_ADMIN").get(), roleService.readById("ROLE_DEVELOPER").get())));
			userService.create(
					new User(
							null,
							"admin",
							"mail",
							"pass",
							"photo",
							List.of(roleService.readById("ROLE_ADMIN").get())));
			userService.create(new User(
					null,
					"dev",
					"mail",
					"pass",
					"photo",
					List.of(roleService.readById("ROLE_DEVELOPER").get())));

//			userService.addRole(1L, roleService.readRoleById("ROLE_ADMIN").orElseThrow());
//			userService.addRole(1L, roleService.readRoleById("ROLE_DEVELOPER").orElseThrow());
//			userService.addRole(2L, roleService.readRoleById("ROLE_ADMIN").orElseThrow());
//			userService.addRole(3L, roleService.readRoleById("ROLE_DEVELOPER").orElseThrow());
		};
	}

}
