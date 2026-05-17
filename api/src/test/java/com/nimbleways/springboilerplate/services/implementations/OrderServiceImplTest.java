package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.handlers.ProductHandler;
import com.nimbleways.springboilerplate.handlers.ProductHandlerSelector;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@UnitTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductHandlerSelector productHandlerSelector;

    @Mock
    private ProductHandler normalProductHandler;

    @Mock
    private ProductHandler seasonalProductHandler;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findOrderByIdReturnsOrderWhenItExists() {
        Order order = new Order(42L, Set.of());
        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));

        Order actualOrder = orderService.findOrderById(42L);

        assertSame(order, actualOrder);
    }

    @Test
    void findOrderByIdThrowsWhenOrderDoesNotExist() {
        when(orderRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(42L));
    }

    @Test
    void processHandlesEveryProductInOrderAndReturnsOrder() {
        Product normalProduct = product("NORMAL", "RJ45 Cable");
        Product seasonalProduct = product("SEASONAL", "Watermelon");
        Order order = new Order(42L, Set.of(normalProduct, seasonalProduct));
        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));
        when(productHandlerSelector.getProductHandler("NORMAL")).thenReturn(normalProductHandler);
        when(productHandlerSelector.getProductHandler("SEASONAL")).thenReturn(seasonalProductHandler);

        Order actualOrder = orderService.process(42L);

        assertSame(order, actualOrder);
        verify(normalProductHandler).handle(normalProduct);
        verify(seasonalProductHandler).handle(seasonalProduct);
    }

    private Product product(String type, String name) {
        return new Product(null, 0, 0, type, name, null, null, null);
    }
}
