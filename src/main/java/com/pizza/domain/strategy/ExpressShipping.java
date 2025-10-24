package com.pizza.domain.strategy;

/**
 * Express shipping strategy.
 * Flat fee of 40,000 VND regardless of order total.
 */
public class ExpressShipping implements ShippingStrategy {
    private static final int EXPRESS_FEE = 40_000;
    
    @Override
    public int calculateFee(int subtotal) {
        return EXPRESS_FEE;
    }
    
    @Override
    public String getName() {
        return "Express Shipping";
    }
    
    @Override
    public String toString() {
        return getName() + " (Flat 40k)";
    }
}









