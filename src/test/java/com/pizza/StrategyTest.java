package com.pizza;

import com.pizza.domain.strategy.CashPayment;
import com.pizza.domain.strategy.CardPayment;
import com.pizza.domain.strategy.EWalletPayment;
import com.pizza.domain.strategy.PaymentStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Payment Strategy pattern implementations.
 * Tests payment processing for different payment methods in POS system.
 * 
 * DEMONSTRATES:
 * - Strategy Pattern: Interchangeable payment algorithms
 * - Liskov Substitution Principle: All strategies can be used polymorphically
 * - Interface Segregation: Focused PaymentStrategy interface
 */
class StrategyTest {

    @Test
    void testCashPaymentProcessing() {
        PaymentStrategy strategy = new CashPayment();

        // Cash payment should always succeed at POS
        assertTrue(strategy.processPayment(50_000), "Cash payment should succeed");
        assertTrue(strategy.processPayment(200_000), "Cash payment should succeed");
        assertTrue(strategy.processPayment(1_000_000), "Cash payment should succeed");
    }

    @Test
    void testCardPaymentProcessing() {
        PaymentStrategy strategy = new CardPayment();

        // Card payment should succeed (in real system, would check with gateway)
        assertTrue(strategy.processPayment(50_000), "Card payment should succeed");
        assertTrue(strategy.processPayment(500_000), "Card payment should succeed");
    }

    @Test
    void testEWalletPaymentProcessing() {
        PaymentStrategy strategy = new EWalletPayment();

        // E-wallet payment should succeed (in real system, would check QR scan)
        assertTrue(strategy.processPayment(50_000), "E-wallet payment should succeed");
        assertTrue(strategy.processPayment(300_000), "E-wallet payment should succeed");
    }

    @Test
    void testPaymentStrategyNames() {
        PaymentStrategy cash = new CashPayment();
        assertEquals("Cash", cash.getName(), "Cash payment name should be 'Cash'");

        PaymentStrategy card = new CardPayment();
        assertEquals("Card", card.getName(), "Card payment name should be 'Card'");

        PaymentStrategy ewallet = new EWalletPayment();
        assertEquals("E-Wallet", ewallet.getName(), "E-Wallet payment name should be 'E-Wallet'");
    }

    @Test
    void testPaymentStrategyDescriptions() {
        PaymentStrategy cash = new CashPayment();
        assertNotNull(cash.getDescription(), "Cash should have description");
        assertTrue(cash.getDescription().contains("quầy"), "Cash description should mention 'quầy'");

        PaymentStrategy card = new CardPayment();
        assertNotNull(card.getDescription(), "Card should have description");

        PaymentStrategy ewallet = new EWalletPayment();
        assertNotNull(ewallet.getDescription(), "E-Wallet should have description");
    }

    @Test
    void testStrategyPolymorphism() {
        // Test Liskov Substitution Principle: all payment strategies can be used
        // interchangeably
        PaymentStrategy[] strategies = {
                new CashPayment(),
                new CardPayment(),
                new EWalletPayment()
        };

        for (PaymentStrategy strategy : strategies) {
            assertNotNull(strategy.getName(), "Strategy should have name");
            assertNotNull(strategy.getDescription(), "Strategy should have description");
            assertTrue(strategy.processPayment(100_000),
                    strategy.getName() + " should process payment successfully");
        }
    }

    @Test
    void testZeroAmountPayment() {
        // Edge case: zero amount (free order or promotion)
        PaymentStrategy cash = new CashPayment();
        assertTrue(cash.processPayment(0), "Should handle zero amount payment");
    }

    @Test
    void testLargeAmountPayment() {
        // Test with large amount
        PaymentStrategy card = new CardPayment();
        assertTrue(card.processPayment(10_000_000), "Should handle large amount payment");
    }
}
