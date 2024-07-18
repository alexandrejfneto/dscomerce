package com.alejfneto.dscomerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alejfneto.dscomerce.dto.OrderDTO;
import com.alejfneto.dscomerce.entities.Order;
import com.alejfneto.dscomerce.entities.OrderItem;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.repositories.OrderItemRepository;
import com.alejfneto.dscomerce.repositories.OrderRepository;
import com.alejfneto.dscomerce.repositories.ProductRepository;
import com.alejfneto.dscomerce.services.exceptions.ForbiddenException;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;
import com.alejfneto.dscomerce.tests.OrderFactory;
import com.alejfneto.dscomerce.tests.ProductFactory;
import com.alejfneto.dscomerce.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
	
	@InjectMocks
	private OrderService service;
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;
	
	private Long existingOrderId, nonExistingOrderId;
	private Long existingProductId, nonExistingProductId;
	private Order order;
	private OrderDTO orderDTO;
	private User adminUser, clientUser;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingOrderId = 1L;
		nonExistingOrderId = 2L;
		
		existingProductId = 1L;
		nonExistingProductId = 2L;
		
		adminUser = UserFactory.createAdminUser();
		clientUser = UserFactory.createClientUser();
		
		order = OrderFactory.createCustomOrder(clientUser);
		orderDTO = new OrderDTO(order);
		
		product = ProductFactory.createProduct();
		
		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(any())).thenReturn(order);
		
		Mockito.when(orderItemRepository.save(any())).thenReturn(new ArrayList<>(order.getItems()));
		
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
	
	@Test
	public void insertShouldReturnOrderDTOWhenAdminAuthenticated() {
		
		Mockito.when(userService.authenticated()).thenReturn(adminUser);
		
		OrderDTO result = service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenClientAuthenticated() {
		
		Mockito.when(userService.authenticated()).thenReturn(clientUser);
		
		OrderDTO result = service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void insertShouldThrowsUserNotFoundExceptionWhenUserDoesNotAuthenticated() {
		
		Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
		
		order.setClient(new User());
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			service.insert(orderDTO);
		});
		
	}
	
	@Test
	public void insertShouldThrowsResourceNotFoundExceptionWhenOrderProductIdDoesNotExist() {
		
		Mockito.when(userService.authenticated()).thenReturn(clientUser);
		
		product.setId(nonExistingOrderId);
		OrderItem item = new OrderItem (order, product, 2, 10.0);
		order.getItems().add(item);
		OrderDTO orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.insert(orderDTO);
		});
		
	}

}
