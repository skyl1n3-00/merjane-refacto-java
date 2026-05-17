package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.handlers.ProductHandler;
import com.nimbleways.springboilerplate.handlers.ProductHandlerSelector;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductHandlerSelector productHandlerSelector;

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public Order process(Long orderId) {
        Order order = this.findOrderById(orderId);
        Set<Product> products = order.getItems();
        for (Product product : products) {
            ProductHandler productHandler = productHandlerSelector.getProductHandler(product.getType());
            productHandler.handle(product);
        }
        return order;
    }

}
