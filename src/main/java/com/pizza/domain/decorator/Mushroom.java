package com.pizza.domain.decorator;

import com.pizza.domain.pizza.Pizza;

/**
 * Concrete Decorator - Mushroom topping.
 * Adds mushrooms to any pizza for 8,000 VND.
 */
public class Mushroom extends ToppingDecorator {
    private static final int MUSHROOM_PRICE = 8_000;

    public Mushroom(Pizza pizza) {
        super(pizza, "Mushroom", MUSHROOM_PRICE);
    }
}






