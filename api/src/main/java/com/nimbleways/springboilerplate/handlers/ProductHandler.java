package com.nimbleways.springboilerplate.handlers;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductHandler {
    void handle(Product product);
    String productType();
}
