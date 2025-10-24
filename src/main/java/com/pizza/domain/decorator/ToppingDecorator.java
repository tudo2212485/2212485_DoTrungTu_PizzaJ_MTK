package com.pizza.domain.decorator;

import com.pizza.domain.pizza.Pizza;

/**
 * Abstract Decorator for adding toppings to pizzas.
 * DESIGN PATTERN: Decorator Pattern
 * 
 * This allows us to dynamically add responsibilities (toppings) to pizza
 * objects
 * without modifying their structure. Each topping wraps a pizza and adds its
 * own price and description.
 */
public abstract class ToppingDecorator extends Pizza {
    protected Pizza pizza;
    protected String toppingName;
    protected int toppingPrice;

    public ToppingDecorator(Pizza pizza, String toppingName, int toppingPrice) {
        super(pizza.getName(), pizza.getSize(), pizza.getBasePrice());
        this.pizza = pizza;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    @Override
    public int getPrice() {
        return pizza.getPrice() + toppingPrice;
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " + " + toppingName;
    }

    /**
     * Get the list of toppings added to this pizza.
     * Recursively builds the topping list.
     */
    public String getToppings() {
        String baseToppings = "";
        if (pizza instanceof ToppingDecorator) {
            baseToppings = ((ToppingDecorator) pizza).getToppings() + ", ";
        }
        return baseToppings + toppingName;
    }

    /**
     * Get the wrapped pizza (base pizza or another decorator).
     * Used for unwrapping decorators to get the original pizza.
     */
    public Pizza getWrappedPizza() {
        return pizza;
    }
}
