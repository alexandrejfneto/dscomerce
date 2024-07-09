package com.alejfneto.dscomerce.tests;

import java.time.LocalDate;

import com.alejfneto.dscomerce.entities.Role;
import com.alejfneto.dscomerce.entities.User;

public class UserFactory {
	
	public static User createClientUser() {
		User user = new User(1L, "maria", "maria@gmail.com", "34988552255", LocalDate.parse("2001-07-25"), "$2a$10$o/FpGQYQfz/DnLnGH4dwd.xCHGd1KUR3vxaQxqo3ukME/E2k0.VrC");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		return user;
	}
	
	public static User createAdminUser() {
		User user = new User(2L, "alex", "alex@gmail.com", "34988552255", LocalDate.parse("2001-07-25"), "$2a$10$o/FpGQYQfz/DnLnGH4dwd.xCHGd1KUR3vxaQxqo3ukME/E2k0.VrC");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		return user;
	}
	
	public static User createCustomClientUser(Long id, String username) {
		User user = new User(id, "maria", username, "34988552255", LocalDate.parse("2001-07-25"), "$2a$10$o/FpGQYQfz/DnLnGH4dwd.xCHGd1KUR3vxaQxqo3ukME/E2k0.VrC");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		return user;
	}
	
	public static User createCustomAdminUser(Long id, String username) {
		User user = new User(id, "alex", username, "34988552255", LocalDate.parse("2001-07-25"), "$2a$10$o/FpGQYQfz/DnLnGH4dwd.xCHGd1KUR3vxaQxqo3ukME/E2k0.VrC");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		return user;
	}

}
