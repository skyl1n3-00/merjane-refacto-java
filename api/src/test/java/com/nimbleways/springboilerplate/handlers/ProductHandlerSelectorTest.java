package com.nimbleways.springboilerplate.handlers;

import com.nimbleways.springboilerplate.exceptions.OrderTypeUnknownException;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ProductHandlerSelectorTest {

    @Test
    void getProductHandlerReturnsMatchingHandler() {
        ProductHandler normalProductHandler = handlerFor("NORMAL");
        ProductHandler seasonalProductHandler = handlerFor("SEASONAL");
        ProductHandlerSelector selector = new ProductHandlerSelector(List.of(normalProductHandler, seasonalProductHandler));

        ProductHandler productHandler = selector.getProductHandler("SEASONAL");

        assertSame(seasonalProductHandler, productHandler);
    }

    @Test
    void getProductHandlerThrowsWhenTypeIsUnknown() {
        ProductHandlerSelector selector = new ProductHandlerSelector(List.of(handlerFor("NORMAL")));

        assertThrows(OrderTypeUnknownException.class, () -> selector.getProductHandler("UNKNOWN"));
    }

    private ProductHandler handlerFor(String productType) {
        ProductHandler productHandler = mock(ProductHandler.class);
        when(productHandler.productType()).thenReturn(productType);
        return productHandler;
    }
}
