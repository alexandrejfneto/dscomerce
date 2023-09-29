package com.alejfneto.dscomerce.dto;

import com.alejfneto.dscomerce.entities.Product;

public class ProductMinDTO {
	
	private Long id;
	private String name;
	private Double price;
	private String imgUrl;
	
	public ProductMinDTO(Long id, String name, Double price, String imgUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
	}
	
	public ProductMinDTO(Product p) {
		id = p.getId();
		name = p.getName();
		price = p.getPrice();
		imgUrl = p.getImgUrl();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

}
