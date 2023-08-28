package com.alejfneto.dscomerce.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.services.ProductService;

@RestController
@RequestMapping (value = "/products")
public class ProductControler {
	
	@Autowired
	private ProductService service;
	
	@GetMapping (value = "/{id}")
	public ProductDTO findById(@PathVariable Long id) {
		return service.findById(id);	
	}
}
