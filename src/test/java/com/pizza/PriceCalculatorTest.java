package com.pizza;

import com.pizza.app.PriceCalculator;
import com.pizza.domain.decorator.Bacon;
import com.pizza.domain.decorator.Cheese;
import com.pizza.domain.decorator.Mushroom;
import com.pizza.domain.factory.PizzaFactory;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Size;
import com.pizza.domain.strategy.ExpressShipping;
import com.pizza.domain.strategy.StandardShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator.
 * Tests pricing calculations with decorators and shipping strategies.
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
    void testTotalWithStandardShippingBelowThreshold() {
        // Subtotal: 75,000 (below 200,000)
        // Standard shipping: 20,000
        // Total: 95,000
        Pizza pizza = PizzaFactory.create("Margherita", Size.M);
        items.add(pizza);
        
        int total = calculator.calculateTotal(items, new StandardShipping());
        assertEquals(95_000, total, "Total with standard shipping should be 95,000");
    }
    
    @Test
    void testTotalWithStandardShippingAboveThreshold() {
        // Pepperoni L: 120,000
        Pizza pizza1 = PizzaFactory.create("Pepperoni", Size.L);
        items.add(pizza1);
        
        // Hawaiian L: 110,000
        Pizza pizza2 = PizzaFactory.create("Hawaiian", Size.L);
        items.add(pizza2);
        
        // Subtotal: 230,000 (above 200,000)
        // Standard shipping: 0 (free)
        // Total: 230,000
        int total = calculator.calculateTotal(items, new StandardShipping());
        assertEquals(230_000, total, "Total with free standard shipping should be 230,000");
    }
    
    @Test
    void testTotalWithExpressShipping() {
        // Margherita M: 75,000
        // Express shipping: 40,000
        // Total: 115,000
        Pizza pizza = PizzaFactory.create("Margherita", Size.M);
        items.add(pizza);
        
        int total = calculator.calculateTotal(items, new ExpressShipping());
        assertEquals(115_000, total, "Total with express shipping should be 115,000");
    }
    
    @Test
    void testShippingCalculation() {
        int belowThreshold = calculator.calculateShipping(100_000, new StandardShipping());
        assertEquals(20_000, belowThreshold, "Standard shipping below threshold should be 20,000");
        
        int aboveThreshold = calculator.calculateShipping(200_000, new StandardShipping());
        assertEquals(0, aboveThreshold, "Standard shipping at threshold should be 0");
        
        int express = calculator.calculateShipping(100_000, new ExpressShipping());
        assertEquals(40_000, express, "Express shipping should always be 40,000");
    }
}









