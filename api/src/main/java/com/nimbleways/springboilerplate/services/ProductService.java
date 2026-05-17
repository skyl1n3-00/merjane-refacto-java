package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductService {
    void save(Product product);
    void order(Product product);
    void setOutOfStock(Product product);
    void notifyDelay(int leadTime, Product product);
}
