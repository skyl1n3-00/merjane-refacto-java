package com.nimbleways.springboilerplate.handlers.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.handlers.ProductHandler;
import com.nimbleways.springboilerplate.services.ProductService;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExpiringProductHandler implements ProductHandler {

    private final ProductService productService;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        if (product.getAvailable() > 0 && product.isFresh()) {
            productService.order(product);
        } else {
            if (product.getAvailable() > 0 && product.isFresh()) {
                productService.order(product);
            } else {
                notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
                productService.setOutOfStock(product);
            }
        }
    }

    @Override
    public String productType() {
        return "EXPIRABLE";
    }
}
