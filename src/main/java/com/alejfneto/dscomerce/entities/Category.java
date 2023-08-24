package com.alejfneto.dscomerce.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Category {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
}
