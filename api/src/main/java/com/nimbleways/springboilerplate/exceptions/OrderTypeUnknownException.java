package com.nimbleways.springboilerplate.exceptions;

public class OrderTypeUnknownException extends RuntimeException {
    public OrderTypeUnknownException(String orderType) {
        super("Order type " + orderType + " is unknown.");
    }
}
