package com.pizza.app;

import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.strategy.ShippingStrategy;

import java.util.List;

/**
 * Service for calculating order prices.
 * Demonstrates single responsibility and uses Strategy pattern for shipping.
 */
public class PriceCalculator {
    
    /**
     * Calculate subtotal from list of pizzas.
     * 
     * @param items List of pizza items (potentially decorated with toppings)
     * @return Subtotal in VND
     */
    public int calculateSubtotal(List<Pizza> items) {
        return items.stream()
                .mapToInt(Pizza::getPrice)
                .sum();
    }
    
    /**
     * Calculate shipping fee using the provided strategy.
     * 
     * @param subtotal Order subtotal
     * @param strategy Shipping strategy to use
     * @return Shipping fee in VND
     */
    public int calculateShipping(int subtotal, ShippingStrategy strategy) {
        if (strategy == null) {
            return 0;
        }
        return strategy.calculateFee(subtotal);
    }
    
    /**
     * Calculate total order price (subtotal + shipping).
     * 
     * @param items List of pizza items
     * @param strategy Shipping strategy
     * @return Total price in VND
     */
    public int calculateTotal(List<Pizza> items, ShippingStrategy strategy) {
        int subtotal = calculateSubtotal(items);
        int shipping = calculateShipping(subtotal, strategy);
        return subtotal + shipping;
    }
}









