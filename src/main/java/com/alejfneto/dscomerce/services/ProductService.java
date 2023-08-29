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
	
	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product product = new Product (null, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getImgUrl());
		product = repository.save(product);
		return new ProductDTO(product);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product product = repository.getReferenceById(id);
		copyDTOtoEntity(productDTO, product);
		product = repository.save(product);
		return new ProductDTO(product);
	}
	
	private void copyDTOtoEntity (ProductDTO productDTO, Product product ) {

		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setImgUrl(productDTO.getImgUrl());
		product.setPrice(productDTO.getPrice());
	}
}
