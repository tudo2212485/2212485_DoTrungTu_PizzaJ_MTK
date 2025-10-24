package com.pizza.domain.strategy;

/**
 * Strategy interface for shipping fee calculation.
 * DESIGN PATTERN: Strategy Pattern
 * 
 * Defines a family of algorithms (shipping fee calculations) and makes them
 * interchangeable. This allows the shipping method to vary independently
 * from clients that use it.
 */
public interface ShippingStrategy {
    
    /**
     * Calculate shipping fee based on order subtotal.
     * 
     * @param subtotal The order subtotal in VND
     * @return The shipping fee in VND
     */
    int calculateFee(int subtotal);
    
    /**
     * Get the name of this shipping strategy.
     */
    String getName();
}









