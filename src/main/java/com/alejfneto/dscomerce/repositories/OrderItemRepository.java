package com.alejfneto.dscomerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejfneto.dscomerce.entities.OrderItem;
import com.alejfneto.dscomerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
