package com.alejfneto.dscomerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.dto.ProductMinDTO;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.repositories.ProductRepository;
import com.alejfneto.dscomerce.services.exceptions.DataBaseException;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional (readOnly = true)
	public ProductDTO findById(Long id) {
		Optional <Product> opt = repository.findById(id);
		Product product = opt.orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado!"));
		ProductDTO productDTO = new ProductDTO(product);
		return productDTO;		
	}
	
	@Transactional (readOnly = true)
	public Page <ProductMinDTO> findALL(String name, Pageable pageable) {
		Page <Product> listResult = repository.searchByName(name, pageable);
		Page <ProductMinDTO> listResultFinal = listResult.map(x -> new ProductMinDTO(x));
		return listResultFinal;		
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		//Metodo Alternativo com instanciacao direta no construtor
		Product product = new Product (null, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getImgUrl());
		product = repository.save(product);
		return new ProductDTO(product);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
		Product product = repository.getReferenceById(id);
		copyDTOtoEntity(productDTO, product);
		product = repository.save(product);
		return new ProductDTO(product);
		} catch (EntityNotFoundException e){
			throw new ResourceNotFoundException("Recurso não encontrado!");			
		}
	}
	
	@Transactional (propagation = Propagation.SUPPORTS)
	public void deletById(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DataBaseException("Falha de integridade referencial!");
	   	}	
	}
	
	private void copyDTOtoEntity (ProductDTO productDTO, Product product ) {
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setImgUrl(productDTO.getImgUrl());
		product.setPrice(productDTO.getPrice());
	}
}
