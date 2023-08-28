package com.alejfneto.dscomerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejfneto.dscomerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	

}
