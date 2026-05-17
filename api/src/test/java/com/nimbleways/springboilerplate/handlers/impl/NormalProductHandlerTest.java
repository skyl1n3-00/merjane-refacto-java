package com.nimbleways.springboilerplate.handlers.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.ProductService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@UnitTest
class NormalProductHandlerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private NormalProductHandler normalProductHandler;

    @Test
    void handleOrdersProductWhenItHasStock() {
        Product product = product(3, 0);

        normalProductHandler.handle(product);

        verify(productService).order(product);
    }

    @Test
    void handleNotifiesDelayWhenProductIsOutOfStockAndHasLeadTime() {
        Product product = product(0, 5);

        normalProductHandler.handle(product);

        verify(productService).notifyDelay(5, product);
    }

    @Test
    void handleDoesNothingWhenProductIsOutOfStockWithoutLeadTime() {
        Product product = product(0, 0);

        normalProductHandler.handle(product);

        verifyNoInteractions(productService);
    }

    @Test
    void productTypeIsNormal() {
        assertEquals("NORMAL", normalProductHandler.productType());
    }

    private Product product(int available, int leadTime) {
        return new Product(null, leadTime, available, "NORMAL", "RJ45 Cable", null, null, null);
    }
}
