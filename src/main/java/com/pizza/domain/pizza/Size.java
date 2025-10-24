package com.pizza.domain.pizza;

/**
 * Enum representing pizza sizes with associated price modifiers.
 * Demonstrates encapsulation of size-specific pricing logic.
 */
public enum Size {
    S("Small", 0),
    M("Medium", 15_000),
    L("Large", 30_000);

    private final String displayName;
    private final int priceModifier;

    Size(String displayName, int priceModifier) {
        this.displayName = displayName;
        this.priceModifier = priceModifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriceModifier() {
        return priceModifier;
    }

    @Override
    public String toString() {
        return displayName;
    }
}






