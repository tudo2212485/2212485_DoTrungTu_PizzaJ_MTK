package com.pizza;

import com.pizza.app.PriceCalculator;
import com.pizza.domain.decorator.Bacon;
import com.pizza.domain.decorator.Cheese;
import com.pizza.domain.decorator.Mushroom;
import com.pizza.domain.factory.PizzaFactory;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator in POS system.
 * Tests price calculations with decorators.
 * 
 * DEMONSTRATES:
 * - Single Responsibility: PriceCalculator only handles calculations
 * - Decorator Pattern: Toppings add to price dynamically
 */
class PriceCalculatorTest {

    private PriceCalculator calculator;
    private List<Pizza> items;

    @BeforeEach
    void setUp() {
        calculator = new PriceCalculator();
        items = new ArrayList<>();
    }

    @Test
    void testSubtotalWithSinglePizza() {
        // Margherita Medium: 60,000 + 15,000 = 75,000
        Pizza pizza = PizzaFactory.create("Margherita", Size.M);
        items.add(pizza);

        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(75_000, subtotal, "Margherita M should be 75,000");
    }

    @Test
    void testSubtotalWithTwoToppings() {
        // Pepperoni Large: 90,000 + 30,000 = 120,000
        // + Cheese: 10,000
        // + Bacon: 15,000
        // Total: 145,000
        Pizza pizza = PizzaFactory.create("Pepperoni", Size.L);
        pizza = new Cheese(pizza);
        pizza = new Bacon(pizza);
        items.add(pizza);

        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(145_000, subtotal, "Pepperoni L + Cheese + Bacon should be 145,000");
    }

    @Test
    void testSubtotalWithMultiplePizzas() {
        // Margherita S: 60,000 + 0 = 60,000
        Pizza pizza1 = PizzaFactory.create("Margherita", Size.S);
        items.add(pizza1);

        // Hawaiian M + Mushroom: 80,000 + 15,000 + 8,000 = 103,000
        Pizza pizza2 = PizzaFactory.create("Hawaiian", Size.M);
        pizza2 = new Mushroom(pizza2);
        items.add(pizza2);

        // Total: 60,000 + 103,000 = 163,000
        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(163_000, subtotal, "Two pizzas should total 163,000");
    }

    @Test
    void testSubtotalWithAllToppings() {
        // Seafood M: 120,000 + 15,000 = 135,000
        // + Cheese: 10,000
        // + Bacon: 15,000
        // + Mushroom: 8,000
        // Total: 168,000
        Pizza pizza = PizzaFactory.create("Seafood", Size.M);
        pizza = new Cheese(pizza);
        pizza = new Bacon(pizza);
        pizza = new Mushroom(pizza);
        items.add(pizza);

        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(168_000, subtotal, "Seafood M with all toppings should be 168,000");
    }

    @Test
    void testEmptyCart() {
        // Empty cart should have 0 total
        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(0, subtotal, "Empty cart should have 0 total");
    }

    @Test
    void testTaxCalculation() {
        // Test tax calculation (if needed for future)
        int subtotal = 100_000;
        int tax = calculator.calculateTax(subtotal, 0.1); // 10% tax
        assertEquals(10_000, tax, "10% tax on 100,000 should be 10,000");
    }

    @Test
    void testDiscountCalculation() {
        // Test discount calculation
        int subtotal = 200_000;
        int discount = calculator.calculateDiscount(subtotal, 10); // 10% discount
        assertEquals(20_000, discount, "10% discount on 200,000 should be 20,000");
    }

    @Test
    void testNoTaxOnZero() {
        int tax = calculator.calculateTax(0, 0.1);
        assertEquals(0, tax, "Tax on 0 should be 0");
    }

    @Test
    void testLargeOrderCalculation() {
        // Add multiple high-value items
        for (int i = 0; i < 5; i++) {
            Pizza pizza = PizzaFactory.create("Seafood", Size.L);
            pizza = new Cheese(pizza);
            pizza = new Bacon(pizza);
            items.add(pizza);
        }

        // Seafood L: 120,000 + 30,000 + 10,000 + 15,000 = 175,000 each
        // 5 pizzas: 875,000
        int subtotal = calculator.calculateSubtotal(items);
        assertEquals(875_000, subtotal, "5 Seafood L with toppings should be 875,000");
    }
}
