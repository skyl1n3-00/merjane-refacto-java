package com.nimbleways.springboilerplate.handlers.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.ProductService;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ExpiringProductHandlerTest {

    @Mock
    private ProductService productService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ExpiringProductHandler expiringProductHandler;

    @Test
    void handleOrdersProductWhenItHasStockAndIsFresh() {
        Product product = product(3, LocalDate.now().plusDays(1));

        expiringProductHandler.handle(product);

        verify(productService).order(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void handleSendsExpirationNotificationAndSetsOutOfStockWhenProductIsExpired() {
        LocalDate expiryDate = LocalDate.now().minusDays(1);
        Product product = product(3, expiryDate);

        expiringProductHandler.handle(product);

        verify(notificationService).sendExpirationNotification("Yogurt", expiryDate);
        verify(productService).setOutOfStock(product);
    }

    @Test
    void handleSendsExpirationNotificationAndSetsOutOfStockWhenProductHasNoStock() {
        LocalDate expiryDate = LocalDate.now().plusDays(1);
        Product product = product(0, expiryDate);

        expiringProductHandler.handle(product);

        verify(notificationService).sendExpirationNotification("Yogurt", expiryDate);
        verify(productService).setOutOfStock(product);
    }

    @Test
    void productTypeIsExpirable() {
        assertEquals("EXPIRABLE", expiringProductHandler.productType());
    }

    private Product product(int available, LocalDate expiryDate) {
        return new Product(null, 0, available, "EXPIRABLE", "Yogurt", expiryDate, null, null);
    }
}
