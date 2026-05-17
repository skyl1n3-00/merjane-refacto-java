package com.nimbleways.springboilerplate.handlers.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.handlers.ProductHandler;
import com.nimbleways.springboilerplate.services.ProductService;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SeasonalProductHandler implements ProductHandler {

    private final ProductService productService;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        // Add new season rules
        if (product.isInSeason() && product.hasStock()) {
            productService.order(product);
        } else {
            if (LocalDate.now().plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
                notificationService.sendOutOfStockNotification(product.getName());
                productService.setOutOfStock(product);
            } else if (product.getSeasonStartDate().isAfter(LocalDate.now())) {
                notificationService.sendOutOfStockNotification(product.getName());
                productService.save(product);
            } else {
                productService.notifyDelay(product.getLeadTime(), product);
            }
        }
    }

    public String productType() {
        return "SEASONAL";
    }
}
