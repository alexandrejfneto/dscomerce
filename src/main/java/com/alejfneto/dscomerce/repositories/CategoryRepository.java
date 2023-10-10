package com.alejfneto.dscomerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejfneto.dscomerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
