package com.pizza.domain.strategy;

/**
 * Card payment strategy for POS system.
 * DESIGN PATTERN: Strategy Pattern (Concrete Strategy)
 * 
 * Handles credit/debit card payment processing.
 * In a real POS system, this would integrate with card reader/payment gateway.
 * 
 * SOLID PRINCIPLES:
 * - Single Responsibility: Only handles card payment logic
 * - Open/Closed: Can extend without modifying existing code
 * - Liskov Substitution: Can replace any PaymentStrategy
 */
public class CardPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(int amount) {
        // In real system: Connect to payment gateway, process card
        System.out.println("💳 Card payment processing: " + amount + " VND");
        // Simulate successful payment
        return true;
    }

    @Override
    public String getName() {
        return "Card";
    }

    @Override
    public String getDescription() {
        return "Thẻ tín dụng/ghi nợ - Visa, Mastercard";
    }

    @Override
    public String toString() {
        return getName() + " (" + getDescription() + ")";
    }
}

