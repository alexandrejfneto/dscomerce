package com.alejfneto.dscomerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.dto.ProductMinDTO;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.repositories.ProductRepository;
import com.alejfneto.dscomerce.services.exceptions.DataBaseException;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;
import com.alejfneto.dscomerce.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private Product product;
	private ProductDTO productDTO;
	private Long existingId, nonExistingId, dependentId;
	private String productName;
	
	private PageImpl<Product> page;

	@BeforeEach
	void setup() throws Exception {

		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		productName = "Playstation 5";

		product = ProductFactory.createProduct(productName);
		productDTO = new ProductDTO (product);
		
		page = new PageImpl<>(List.of(product));

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.searchByName(any(), (Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}

	@Test
	public void findByIdShoulReturnProductDTOWhenIdExist() {

		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());
		Assertions.assertEquals(result.getName(), product.getName());

	}

	@Test
	public void findByIdShoulThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO result = service.findById(nonExistingId);
		});

	}
	
	@Test
	public void findAllShouldReturnPagedProductMinDTO() {
		Pageable pageable = PageRequest.of(0, 12);
		String name = "Playstation 5";
		
		Page<ProductMinDTO> result = service.findALL(name, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getName(), product.getName());
	}
	
	@Test
	public void insertShouldReturnProductDTO () {
		
		//ProductService serviceSpy = Mockito.spy(service);
		
		ProductDTO result = service.insert(productDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getName(), product.getName());
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExist() {
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getName(), product.getName());
		Assertions.assertEquals(result.getId(), product.getId());
		
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO result = service.update(nonExistingId, productDTO);
		});
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()-> {
			service.deleteById(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
			service.deleteById(nonExistingId);
		});
		
	}
	
	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
		
		Assertions.assertThrows(DataBaseException.class, ()-> {
			service.deleteById(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
		
	}
	
}
