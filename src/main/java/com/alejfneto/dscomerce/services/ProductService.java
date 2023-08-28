package com.alejfneto.dscomerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional (readOnly = true)
	public ProductDTO findById(Long id) {
		Optional <Product> opt = repository.findById(id);
		Product product = opt.get();
		ProductDTO productDTO = new ProductDTO(product);
		return productDTO;		
	}
	
	@Transactional (readOnly = true)
	public Page <ProductDTO> findAll(Pageable pageable) {
		Page <Product> listResult = repository.findAll(pageable);
		Page <ProductDTO> listResultFinal = listResult.map(x -> new ProductDTO(x));
		return listResultFinal;		
	}

}
