package com.alejfneto.dscomerce.controlers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.repositories.ProductRepository;

@RestController
@RequestMapping (value = "/products")
public class ProductControler {
	
	@Autowired
	private ProductRepository repository;
	
	@GetMapping
	public String teste() {
		Optional <Product> opt = repository.findById(1L);
		Product product = opt.get();
		return product.getName();		
	}
}
