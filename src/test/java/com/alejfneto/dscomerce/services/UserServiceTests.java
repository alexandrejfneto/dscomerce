package com.alejfneto.dscomerce.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.projections.UserDetailsProjection;
import com.alejfneto.dscomerce.repositories.UserRepository;
import com.alejfneto.dscomerce.tests.UserDetailsFactory;
import com.alejfneto.dscomerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;
	
	private String existingUserName, nonExistingUserName;
	private User user;
	private List<UserDetailsProjection> userDetails;
	
	@BeforeEach
	void setup() throws Exception {
		
		existingUserName = "usuario01@gmail.com";
		nonExistingUserName = "usuario02@gmail.com";
		
		user = UserFactory.createCustomClientUser(1L, existingUserName);
		
		userDetails = UserDetailsFactory.createCustomAdminUserDetails(existingUserName);
		
		Mockito.when(repository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUserName)).thenReturn(new ArrayList<>());
		
	}
	
	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserNameExists() {
		
		UserDetails result = service.loadUserByUsername(existingUserName);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUserName);
		
	}
	
	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserNameDoesNotExists() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()-> {
			service.loadUserByUsername(nonExistingUserName);
		});
		Mockito.verify(repository, Mockito.times(1)).searchUserAndRolesByEmail(nonExistingUserName);
		
	}

}
