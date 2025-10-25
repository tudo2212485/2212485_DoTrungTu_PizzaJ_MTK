package com.pizza.app;

import com.pizza.domain.pizza.Pizza;

import java.util.List;

/**
 * Service for calculating order prices in POS system.
 * 
 * SOLID PRINCIPLES APPLIED:
 * - Single Responsibility: Only handles price calculation logic
 * - Open/Closed: Can extend for new calculation types without modification
 */
public class PriceCalculator {

    /**
     * Calculate total from list of pizzas.
     * For POS system, this is the order total - no shipping fees.
     * 
     * @param items List of pizza items (potentially decorated with toppings)
     * @return Total in VND
     */
    public int calculateSubtotal(List<Pizza> items) {
        return items.stream()
                .mapToInt(Pizza::getPrice)
                .sum();
    }

    /**
     * Calculate tax amount (if applicable).
     * Can be extended for different tax rates.
     * 
     * @param subtotal Order subtotal
     * @param taxRate  Tax rate (e.g., 0.1 for 10%)
     * @return Tax amount in VND
     */
    public int calculateTax(int subtotal, double taxRate) {
        return (int) (subtotal * taxRate);
    }

    /**
     * Calculate discount amount.
     * Can be extended with different discount strategies.
     * 
     * @param subtotal        Order subtotal
     * @param discountPercent Discount percentage (e.g., 10 for 10%)
     * @return Discount amount in VND
     */
    public int calculateDiscount(int subtotal, double discountPercent) {
        return (int) (subtotal * discountPercent / 100);
    }
}
