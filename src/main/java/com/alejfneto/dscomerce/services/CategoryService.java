package com.alejfneto.dscomerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.CategoryDTO;
import com.alejfneto.dscomerce.entities.Category;
import com.alejfneto.dscomerce.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional (readOnly = true)
	public List <CategoryDTO> findAll() {
		List<Category> listResult = repository.findAll();
		List <CategoryDTO> listResultFinal = listResult.stream().map(x -> new CategoryDTO(x)).toList();
		return listResultFinal;		
	}

}
