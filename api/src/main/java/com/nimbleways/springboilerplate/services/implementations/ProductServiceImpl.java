package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void order(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        this.save(product);
    }

    @Override
    public void setOutOfStock(Product product) {
        product.setAvailable(0);
        this.save(product);
    }

    private void setLeadTime(int leadTime, Product product) {
        product.setLeadTime(leadTime);
        this.save(product);
    }

    @Override
    public void notifyDelay(int leadTime, Product product) {
        setLeadTime(leadTime, product);
        notificationService.sendDelayNotification(leadTime, product.getName());
    }
}