package com.alejfneto.dscomerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.services.exceptions.ForbiddenException;
import com.alejfneto.dscomerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

	@InjectMocks
	private AuthService service;

	@Mock
	private UserService userService;

	private User userAdmin, userSelfClient, userOtherClient;

	@BeforeEach
	void setUp() throws Exception {

		userAdmin = UserFactory.createAdminUser();
		userSelfClient = UserFactory.createCustomClientUser(1L, "Bob");
		userOtherClient = UserFactory.createCustomClientUser(2L, "Ana");

	}

	@Test
	public void validateSelfOrAdminShouldDoNothingWhenUserAdminAuthencicated() {

		Mockito.when(userService.authenticated()).thenReturn(userAdmin);

		Long userId = userAdmin.getId();

		Assertions.assertDoesNotThrow(() -> 
			service.validateSelfOrAdmin(userId)
		);

	}
	
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenUserSelfClientAuthencicated() {

		Mockito.when(userService.authenticated()).thenReturn(userSelfClient);

		Long userId = userSelfClient.getId();

		Assertions.assertDoesNotThrow(() -> 
			service.validateSelfOrAdmin(userId)
		);

	}
	
	@Test
	public void validateSelfOrAdminShouldThrowsForbiddenExceptionWhenUserSelfClientOtherAuthencicated() {

		Mockito.when(userService.authenticated()).thenReturn(userSelfClient);

		Long userId = userOtherClient.getId();

		Assertions.assertThrows(ForbiddenException.class, () -> {
			service.validateSelfOrAdmin(userId);
		});

	}

}
