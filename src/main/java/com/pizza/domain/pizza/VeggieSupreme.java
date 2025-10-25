package com.pizza.domain.pizza;

/**
 * Concrete Pizza implementation - Veggie Supreme.
 * Demonstrates inheritance and polymorphism.
 */
public class VeggieSupreme extends Pizza {
    private static final int BASE_PRICE = 70_000;

    public VeggieSupreme(Size size) {
        super("Pizza Chay Rau Củ", size, BASE_PRICE);
    }
}





