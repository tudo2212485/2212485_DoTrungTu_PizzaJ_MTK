package com.pizza.domain.pizza;

/**
 * Concrete Pizza implementation - Seafood.
 * Demonstrates inheritance and polymorphism.
 */
public class Seafood extends Pizza {
    private static final int BASE_PRICE = 120_000;

    public Seafood(Size size) {
        super("Pizza Hải Sản", size, BASE_PRICE);
    }
}





