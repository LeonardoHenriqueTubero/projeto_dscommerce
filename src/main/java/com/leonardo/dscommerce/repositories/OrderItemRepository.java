package com.leonardo.dscommerce.repositories;

import com.leonardo.dscommerce.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import com.leonardo.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}