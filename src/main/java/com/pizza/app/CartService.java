package com.pizza.app;

import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.strategy.PaymentStrategy;
import com.pizza.domain.strategy.CashPayment;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing shopping cart in POS system.
 * Uses Observer pattern to notify UI of cart changes.
 * 
 * SOLID PRINCIPLES APPLIED:
 * - Single Responsibility: Only manages cart state and operations
 * - Dependency Inversion: Depends on PaymentStrategy abstraction, not concrete
 * implementations
 * - Open/Closed: Can add new payment methods without modifying this class
 */
public class CartService {
    private static CartService instance;
    private final List<Pizza> items;
    private PaymentStrategy paymentStrategy;
    private final PriceCalculator priceCalculator;
    private final EventBus eventBus;

    private CartService() {
        this.items = new ArrayList<>();
        this.paymentStrategy = new CashPayment(); // Default payment method for POS
        this.priceCalculator = new PriceCalculator();
        this.eventBus = EventBus.getInstance();
    }

    /**
     * Get singleton instance of CartService.
     */
    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    /**
     * Add a pizza to the cart.
     * Publishes CART_UPDATED event to observers.
     */
    public void addItem(Pizza pizza) {
        items.add(pizza);
        eventBus.publish("CART_UPDATED");
    }

    /**
     * Remove a pizza from the cart by index.
     */
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            eventBus.publish("CART_UPDATED");
        }
    }

    /**
     * Replace a pizza in the cart (for editing).
     */
    public void replaceItem(int index, Pizza newPizza) {
        if (index >= 0 && index < items.size()) {
            items.set(index, newPizza);
            eventBus.publish("CART_UPDATED");
        }
    }

    /**
     * Clear all items from cart.
     */
    public void clear() {
        items.clear();
        eventBus.publish("CART_UPDATED");
    }

    /**
     * Get all items in cart.
     */
    public List<Pizza> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Get number of items in cart.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Set payment strategy.
     * Publishes CART_UPDATED event for UI refresh.
     */
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
        eventBus.publish("CART_UPDATED");
    }

    /**
     * Get current payment strategy.
     */
    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    /**
     * Calculate cart total.
     * For POS system, no shipping fee - just sum of items.
     */
    public int getTotal() {
        return priceCalculator.calculateSubtotal(items);
    }

    /**
     * Process payment for current cart.
     * 
     * @return true if payment successful
     */
    public boolean processPayment() {
        return paymentStrategy.processPayment(getTotal());
    }
}
