package com.leonardo.dscommerce.tests;

import com.leonardo.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder(User user) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        return order;
    }
}
