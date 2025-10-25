package com.pizza.domain.strategy;

/**
 * E-Wallet payment strategy for POS system.
 * DESIGN PATTERN: Strategy Pattern (Concrete Strategy)
 * 
 * Handles electronic wallet payments (MoMo, ZaloPay, etc).
 * In a real POS system, this would integrate with QR code scanner and e-wallet
 * APIs.
 * 
 * SOLID PRINCIPLES:
 * - Single Responsibility: Only handles e-wallet payment logic
 * - Open/Closed: Can extend without modifying existing code
 * - Liskov Substitution: Can replace any PaymentStrategy
 */
public class EWalletPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(int amount) {
        // In real system: Generate QR code, wait for customer scan and confirm
        System.out.println("📱 E-Wallet payment via QR: " + amount + " VND");
        // Simulate successful payment
        return true;
    }

    @Override
    public String getName() {
        return "E-Wallet";
    }

    @Override
    public String getDescription() {
        return "Ví điện tử - MoMo, ZaloPay, VNPay";
    }

    @Override
    public String toString() {
        return getName() + " (" + getDescription() + ")";
    }
}

