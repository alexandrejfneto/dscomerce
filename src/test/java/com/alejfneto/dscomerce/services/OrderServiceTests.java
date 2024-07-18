package com.alejfneto.dscomerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alejfneto.dscomerce.dto.OrderDTO;
import com.alejfneto.dscomerce.entities.Order;
import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.repositories.OrderRepository;
import com.alejfneto.dscomerce.services.exceptions.ForbiddenException;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;
import com.alejfneto.dscomerce.tests.OrderFactory;
import com.alejfneto.dscomerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
	
	@InjectMocks
	private OrderService service;
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	private Long existingOrderId, nonExistingOrderId;
	private Order order;
	private OrderDTO orderDTO;
	private User adminUser, clientUser;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingOrderId = 1L;
		nonExistingOrderId = 2L;
		
		adminUser = UserFactory.createAdminUser();
		clientUser = UserFactory.createClientUser();
		
		order = OrderFactory.createCustomOrder(clientUser);
		orderDTO = new OrderDTO(order);
		
		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenOrderIdExistsAndAdminAuthenticated() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		OrderDTO result = service.findById(existingOrderId);
		
		Assertions.assertNotNull(result);
		
		Assertions.assertEquals(result.getId(), existingOrderId);
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenOrderIdExistsAndClientAuthenticated() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(clientUser.getId());
		
		OrderDTO result = service.findById(existingOrderId);
		
		Assertions.assertNotNull(result);
		
		Assertions.assertEquals(result.getId(), existingOrderId);
		
	}
	
	@Test
	public void findByIdShouldThrowsForbiddenExceptionWhenOrderIdExistsAndOtherClientAuthenticated() {
		
		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(clientUser.getId());
		
		Assertions.assertThrows(ForbiddenException.class, ()-> {
			service.findById(existingOrderId);
		});
		
	}
	
	@Test
	public void findByIdShouldThrowsResourceNotFoundExceptionWhenOrderIdDoesNotExistsAndUserAuthenticated() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
			service.findById(nonExistingOrderId);
		});
		
	}

}
