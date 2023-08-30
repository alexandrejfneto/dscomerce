package com.alejfneto.dscomerce.dto;

import com.alejfneto.dscomerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
	private Long id;
	
	@NotBlank(message = "Campo requerido")
	@Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres")
	private String name;
	
	@Size (min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
	private String description;
	
	@Positive (message = "O preço precisa ser positivo")
	private Double price;
	private String imgUrl;
	
	public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
	}
	
	public ProductDTO(Product p) {
		id = p.getId();
		name = p.getName();
		description = p.getDescription();
		price = p.getPrice();
		imgUrl = p.getImgUrl();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}
	
}
