package com.pizza.app;

import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.strategy.ShippingStrategy;
import com.pizza.domain.strategy.StandardShipping;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing shopping cart.
 * Uses Observer pattern to notify UI of cart changes.
 */
public class CartService {
    private static CartService instance;
    private final List<Pizza> items;
    private ShippingStrategy shippingStrategy;
    private final PriceCalculator priceCalculator;
    private final EventBus eventBus;

    private CartService() {
        this.items = new ArrayList<>();
        this.shippingStrategy = new StandardShipping(); // Default
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
     * Set shipping strategy.
     * Publishes CART_UPDATED event as total changes.
     */
    public void setShippingStrategy(ShippingStrategy strategy) {
        this.shippingStrategy = strategy;
        eventBus.publish("CART_UPDATED");
    }

    /**
     * Get current shipping strategy.
     */
    public ShippingStrategy getShippingStrategy() {
        return shippingStrategy;
    }

    /**
     * Calculate cart subtotal.
     */
    public int getSubtotal() {
        return priceCalculator.calculateSubtotal(items);
    }

    /**
     * Calculate shipping fee.
     */
    public int getShippingFee() {
        return priceCalculator.calculateShipping(getSubtotal(), shippingStrategy);
    }

    /**
     * Calculate cart total (subtotal + shipping).
     */
    public int getTotal() {
        return priceCalculator.calculateTotal(items, shippingStrategy);
    }
}
