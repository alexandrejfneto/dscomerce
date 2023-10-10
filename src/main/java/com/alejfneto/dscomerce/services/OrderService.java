package com.alejfneto.dscomerce.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.OrderDTO;
import com.alejfneto.dscomerce.dto.OrderItemDTO;
import com.alejfneto.dscomerce.entities.Order;
import com.alejfneto.dscomerce.entities.OrderItem;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.enums.OrderStatus;
import com.alejfneto.dscomerce.repositories.OrderItemRepository;
import com.alejfneto.dscomerce.repositories.OrderRepository;
import com.alejfneto.dscomerce.repositories.ProductRepository;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthService authService;

	@Transactional (readOnly = true)
	public OrderDTO findById(Long id) {
		Optional <Order> opt = repository.findById(id);
		Order order = opt.orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado!"));
		OrderDTO orderDTO = new OrderDTO(order);
		authService.validateSelfOrAdmin(order.getClient().getId());
		return orderDTO;
	}

	@Transactional
	public OrderDTO insert(OrderDTO orderDTO) {
		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		order.setClient(userService.authenticated());
		
		for (OrderItemDTO itemDTO : orderDTO.getItems()) {
			Product product = productRepository.getReferenceById(itemDTO.getProductId());
			OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		return new OrderDTO(order);
	}

}
