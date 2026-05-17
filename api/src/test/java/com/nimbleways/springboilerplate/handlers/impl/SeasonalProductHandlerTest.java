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
class SeasonalProductHandlerTest {

    @Mock
    private ProductService productService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SeasonalProductHandler seasonalProductHandler;

    @Test
    void handleOrdersProductWhenItIsInSeasonAndHasStock() {
        Product product = product(3, 2, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        seasonalProductHandler.handle(product);

        verify(productService).order(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void handleNotifiesOutOfStockAndSetsOutOfStockWhenLeadTimePassesSeasonEnd() {
        Product product = product(0, 10, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2));

        seasonalProductHandler.handle(product);

        verify(notificationService).sendOutOfStockNotification("Watermelon");
        verify(productService).setOutOfStock(product);
    }

    @Test
    void handleNotifiesOutOfStockAndSavesProductWhenSeasonHasNotStarted() {
        Product product = product(0, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(10));

        seasonalProductHandler.handle(product);

        verify(notificationService).sendOutOfStockNotification("Watermelon");
        verify(productService).save(product);
    }

    @Test
    void handleNotifiesDelayWhenProductCannotBeOrderedButCanArriveBeforeSeasonEnd() {
        Product product = product(0, 2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10));

        seasonalProductHandler.handle(product);

        verify(productService).notifyDelay(2, product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void productTypeIsSeasonal() {
        assertEquals("SEASONAL", seasonalProductHandler.productType());
    }

    private Product product(int available, int leadTime, LocalDate seasonStartDate, LocalDate seasonEndDate) {
        return new Product(null, leadTime, available, "SEASONAL", "Watermelon", null, seasonStartDate, seasonEndDate);
    }
}
