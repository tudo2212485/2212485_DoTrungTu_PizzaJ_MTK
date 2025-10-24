package com.pizza.domain.pizza;

/**
 * Concrete Pizza implementation - Hawaiian.
 * Demonstrates inheritance and polymorphism.
 */
public class Hawaiian extends Pizza {
    private static final int BASE_PRICE = 80_000;

    public Hawaiian(Size size) {
        super("Hawaiian", size, BASE_PRICE);
    }
}






