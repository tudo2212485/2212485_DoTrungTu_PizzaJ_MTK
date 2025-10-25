package com.pizza.domain.decorator;

import com.pizza.domain.pizza.Pizza;

/**
 * Concrete Decorator - Cheese topping.
 * Adds extra cheese to any pizza for 10,000 VND.
 */
public class Cheese extends ToppingDecorator {
    private static final int CHEESE_PRICE = 10_000;

    public Cheese(Pizza pizza) {
        super(pizza, "Phô Mai", CHEESE_PRICE);
    }
}






