package com.alejfneto.dscomerce.controlers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping (value = "/products")
public class ProductControler {
	
	@Autowired
	private ProductService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
	@GetMapping (value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO productDTO = service.findById(id);
		return ResponseEntity.ok(productDTO);
	}
	
	@GetMapping
	public ResponseEntity <Page <ProductDTO>> findAll(
			@RequestParam (name = "name", defaultValue="") String name, Pageable pageable) {
		Page <ProductDTO> productDTO = service.findALL(name, pageable);
		return ResponseEntity.ok(productDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO productDTO) {
		productDTO = service.insert(productDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(productDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(productDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping (value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,@Valid  @RequestBody ProductDTO productDTO) {
		productDTO = service.update(id, productDTO);
		return ResponseEntity.ok(productDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		service.deletById(id);
		return ResponseEntity.noContent().build();
	}
}
