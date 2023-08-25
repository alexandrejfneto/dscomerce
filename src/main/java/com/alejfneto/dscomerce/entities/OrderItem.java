package com.alejfneto.dscomerce.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
	
	@EmbeddedId
	private OrderItemPK orderItemPK = new OrderItemPK();
	private Integer quantity;
	private Double price;
	
	public OrderItem() {
	}

	public OrderItem(Order order, Product product, Integer quantity, Double price) {
		orderItemPK.setOrder(order);
		orderItemPK.setProduct(product);
		this.quantity = quantity;
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Order getOrder() {
		return orderItemPK.getOrder();
	}
	
	public void setOrder(Order order) {
		orderItemPK.setOrder(order);
	}
	
	public Product getProduct() {
		return orderItemPK.getProduct();
	}
	
	public void setProduct(Product product) {
		orderItemPK.setProduct(product);		
	}
	
}
