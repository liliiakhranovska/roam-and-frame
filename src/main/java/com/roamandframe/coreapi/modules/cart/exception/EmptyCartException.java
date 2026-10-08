package com.roamandframe.coreapi.modules.cart.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cart is empty");
    }
}
