package com.nimbleways.springboilerplate.handlers.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.handlers.ProductHandler;
import com.nimbleways.springboilerplate.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NormalProductHandler implements ProductHandler {

    private final ProductService productService;

    @Override
    public void handle(Product product) {
        if (product.hasStock()) {
            productService.order(product);
        } else {
            int leadTime = product.getLeadTime();
            if (leadTime > 0) {
                productService.notifyDelay(leadTime, product);
            }
        }
    }

    public String productType() {
        return "NORMAL";
    }
}
