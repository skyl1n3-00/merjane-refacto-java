package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void savePersistsProduct() {
        Product product = product("NORMAL", 2, 3, "RJ45 Cable");

        productService.save(product);

        verify(productRepository).save(product);
    }

    @Test
    void orderDecreasesAvailableStockAndPersistsProduct() {
        Product product = product("NORMAL", 2, 3, "RJ45 Cable");

        productService.order(product);

        assertEquals(2, product.getAvailable());
        verify(productRepository).save(product);
    }

    @Test
    void setOutOfStockClearsAvailableStockAndPersistsProduct() {
        Product product = product("NORMAL", 2, 3, "RJ45 Cable");

        productService.setOutOfStock(product);

        assertEquals(0, product.getAvailable());
        verify(productRepository).save(product);
    }

    @Test
    void notifyDelayUpdatesLeadTimePersistsProductAndSendsNotification() {
        Product product = product("NORMAL", 2, 0, "RJ45 Cable");

        productService.notifyDelay(5, product);

        assertEquals(5, product.getLeadTime());
        verify(productRepository).save(product);
        verify(notificationService).sendDelayNotification(5, "RJ45 Cable");
    }

    private Product product(String type, int leadTime, int available, String name) {
        return new Product(null, leadTime, available, type, name, null, null, null);
    }
}
