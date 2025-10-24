package com.pizza.domain.pizza;

/**
 * Concrete Pizza implementation - Margherita.
 * Demonstrates inheritance and polymorphism.
 */
public class Margherita extends Pizza {
    private static final int BASE_PRICE = 60_000;

    public Margherita(Size size) {
        super("Margherita", size, BASE_PRICE);
    }
}






