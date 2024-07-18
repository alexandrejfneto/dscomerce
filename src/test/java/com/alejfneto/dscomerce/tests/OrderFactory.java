package com.alejfneto.dscomerce.tests;

import java.time.Instant;

import com.alejfneto.dscomerce.entities.Order;
import com.alejfneto.dscomerce.entities.OrderItem;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.enums.OrderStatus;

public class OrderFactory {
	
	public static Order createCustomOrder (User user) {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user);
		Product product = ProductFactory.createProduct();
		OrderItem item = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(item);
		return order;
	}

}
