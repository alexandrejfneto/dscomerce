package com.alejfneto.dscomerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.OrderDTO;
import com.alejfneto.dscomerce.entities.Order;
import com.alejfneto.dscomerce.repositories.OrderRepository;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;

	@Transactional (readOnly = true)
	public OrderDTO findById(Long id) {
		Optional <Order> opt = repository.findById(id);
		Order order = opt.orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado!"));
		OrderDTO orderDTO = new OrderDTO(order);
		return orderDTO;
	}

}
