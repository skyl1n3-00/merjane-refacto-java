package com.nimbleways.springboilerplate.handlers;

import com.nimbleways.springboilerplate.exceptions.OrderTypeUnknownException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductHandlerSelector {

    private final Map<String, ProductHandler> productsHandlers;

    public ProductHandlerSelector(List<ProductHandler> productsHandlers) {
        this.productsHandlers = productsHandlers
            .stream()
            .collect(Collectors.toMap(ProductHandler::productType, Function.identity()));
    }

    public ProductHandler getProductHandler(String productType) {
        ProductHandler productHandler = productsHandlers.get(productType);
        if (productHandler == null) {
            throw new OrderTypeUnknownException(productType);
        }
        return productHandler;
    }
}
