package com.pizza.domain.strategy;

/**
 * Standard shipping strategy.
 * Free shipping for orders ≥ 200,000 VND, otherwise 20,000 VND.
 */
public class StandardShipping implements ShippingStrategy {
    private static final int FREE_SHIPPING_THRESHOLD = 200_000;
    private static final int STANDARD_FEE = 20_000;
    
    @Override
    public int calculateFee(int subtotal) {
        return subtotal >= FREE_SHIPPING_THRESHOLD ? 0 : STANDARD_FEE;
    }
    
    @Override
    public String getName() {
        return "Standard Shipping";
    }
    
    @Override
    public String toString() {
        return getName() + " (Free if ≥200k, otherwise 20k)";
    }
}









