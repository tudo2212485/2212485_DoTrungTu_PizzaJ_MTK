package com.pizza.domain.pizza;

/**
 * Abstract base class for all pizzas.
 * Demonstrates abstraction and encapsulation in OOP.
 * This is the Component in the Decorator pattern.
 */
public abstract class Pizza {
    protected String name;
    protected Size size;
    protected int basePrice;

    public Pizza(String name, Size size, int basePrice) {
        this.name = name;
        this.size = size;
        this.basePrice = basePrice;
    }

    /**
     * Calculate the total price of the pizza including size modifier.
     * This method can be overridden by decorators.
     */
    public int getPrice() {
        return basePrice + size.getPriceModifier();
    }

    /**
     * Get the description of the pizza.
     * This method will be enhanced by decorators to add toppings.
     */
    public String getDescription() {
        return name + " (" + size.getDisplayName() + ")";
    }

    public String getName() {
        return name;
    }

    public Size getSize() {
        return size;
    }

    public int getBasePrice() {
        return basePrice;
    }
}






