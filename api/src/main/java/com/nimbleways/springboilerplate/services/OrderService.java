package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Order;

public interface OrderService {
    Order findOrderById(Long orderId);
    Order process(Long orderId);
}
