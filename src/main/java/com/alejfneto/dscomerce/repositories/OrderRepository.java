package com.alejfneto.dscomerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejfneto.dscomerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
