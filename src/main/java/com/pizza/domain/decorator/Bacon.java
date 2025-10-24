package com.pizza.domain.decorator;

import com.pizza.domain.pizza.Pizza;

/**
 * Concrete Decorator - Bacon topping.
 * Adds bacon to any pizza for 15,000 VND.
 */
public class Bacon extends ToppingDecorator {
    private static final int BACON_PRICE = 15_000;

    public Bacon(Pizza pizza) {
        super(pizza, "Bacon", BACON_PRICE);
    }
}






