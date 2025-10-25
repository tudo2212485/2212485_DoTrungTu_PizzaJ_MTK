package com.pizza.domain.strategy;

/**
 * Cash payment strategy for POS system.
 * DESIGN PATTERN: Strategy Pattern (Concrete Strategy)
 * 
 * Handles cash payment processing at the counter.
 * In a real POS system, this would integrate with cash drawer hardware.
 * 
 * SOLID PRINCIPLES:
 * - Single Responsibility: Only handles cash payment logic
 * - Open/Closed: Can extend without modifying existing code
 * - Liskov Substitution: Can replace any PaymentStrategy
 */
public class CashPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(int amount) {
        // In real system: Open cash drawer, wait for confirmation
        System.out.println("💵 Cash payment received: " + amount + " VND");
        return true; // Always successful for cash at POS
    }

    @Override
    public String getName() {
        return "Cash";
    }

    @Override
    public String getDescription() {
        return "Tiền mặt - Thanh toán tại quầy";
    }

    @Override
    public String toString() {
        return getName() + " (" + getDescription() + ")";
    }
}

