package com.pizza.domain.strategy;

/**
 * Strategy interface for payment processing.
 * DESIGN PATTERN: Strategy Pattern
 * 
 * Defines a family of algorithms (payment methods) and makes them
 * interchangeable. This allows the payment method to vary independently
 * from clients that use it.
 * 
 * SOLID PRINCIPLES APPLIED:
 * - Single Responsibility: Only handles payment method behavior
 * - Open/Closed: Can add new payment methods without modifying existing code
 * - Interface Segregation: Small, focused interface
 * - Dependency Inversion: High-level modules depend on this abstraction
 */
public interface PaymentStrategy {

    /**
     * Process payment for an order.
     * 
     * @param amount The order total in VND
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(int amount);

    /**
     * Get the name of this payment method.
     */
    String getName();

    /**
     * Get a description of this payment method.
     */
    String getDescription();
}
