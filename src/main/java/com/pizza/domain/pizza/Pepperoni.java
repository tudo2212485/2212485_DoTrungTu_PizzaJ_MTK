package com.pizza.domain.pizza;

/**
 * Concrete Pizza implementation - Pepperoni.
 * Demonstrates inheritance and polymorphism.
 */
public class Pepperoni extends Pizza {
    private static final int BASE_PRICE = 90_000;

    public Pepperoni(Size size) {
        super("Pizza Pepperoni", size, BASE_PRICE);
    }
}






