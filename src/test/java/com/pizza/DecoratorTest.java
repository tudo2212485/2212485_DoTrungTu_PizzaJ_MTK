package com.pizza;

import com.pizza.domain.decorator.Bacon;
import com.pizza.domain.decorator.Cheese;
import com.pizza.domain.decorator.Mushroom;
import com.pizza.domain.decorator.ToppingDecorator;
import com.pizza.domain.factory.PizzaFactory;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Size;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Decorator pattern implementation.
 * Tests topping decoration and price accumulation.
 */
class DecoratorTest {
    
    @Test
    void testBasePizzaPrice() {
        Pizza margherita = PizzaFactory.create("Margherita", Size.M);
        assertEquals(75_000, margherita.getPrice(), "Margherita M base price should be 75,000");
    }
    
    @Test
    void testSingleToppingDecorator() {
        // Margherita M: 75,000
        // + Cheese: 10,000
        // Total: 85,000
        Pizza pizza = PizzaFactory.create("Margherita", Size.M);
        pizza = new Cheese(pizza);
        
        assertEquals(85_000, pizza.getPrice(), "Pizza with cheese should be 85,000");
        assertTrue(pizza.getDescription().contains("Extra Cheese"), 
                   "Description should mention cheese");
    }
    
    @Test
    void testTwoToppingsIncreasePriceCorrectly() {
        // Hawaiian S: 80,000
        // + Bacon: 15,000
        // + Mushroom: 8,000
        // Total: 103,000
        Pizza pizza = PizzaFactory.create("Hawaiian", Size.S);
        pizza = new Bacon(pizza);
        pizza = new Mushroom(pizza);
        
        assertEquals(103_000, pizza.getPrice(), 
                     "Pizza with bacon and mushroom should be 103,000");
    }
    
    @Test
    void testMultipleToppingsDescription() {
        Pizza pizza = PizzaFactory.create("Pepperoni", Size.L);
        pizza = new Cheese(pizza);
        pizza = new Bacon(pizza);
        pizza = new Mushroom(pizza);
        
        String description = pizza.getDescription();
        assertTrue(description.contains("Pepperoni"), "Should contain pizza name");
        assertTrue(description.contains("Extra Cheese"), "Should contain cheese");
        assertTrue(description.contains("Bacon"), "Should contain bacon");
        assertTrue(description.contains("Mushroom"), "Should contain mushroom");
    }
    
    @Test
    void testToppingsListExtraction() {
        Pizza pizza = PizzaFactory.create("Margherita", Size.M);
        pizza = new Cheese(pizza);
        pizza = new Bacon(pizza);
        
        assertTrue(pizza instanceof ToppingDecorator, "Pizza should be a ToppingDecorator");
        
        String toppings = ((ToppingDecorator) pizza).getToppings();
        assertTrue(toppings.contains("Extra Cheese"), "Toppings should include cheese");
        assertTrue(toppings.contains("Bacon"), "Toppings should include bacon");
    }
    
    @Test
    void testDecoratorChaining() {
        // Test that decorators can be chained in any order
        Pizza pizza1 = PizzaFactory.create("Margherita", Size.S);
        pizza1 = new Cheese(new Bacon(new Mushroom(pizza1)));
        
        Pizza pizza2 = PizzaFactory.create("Margherita", Size.S);
        pizza2 = new Mushroom(new Bacon(new Cheese(pizza2)));
        
        // Price should be the same regardless of order
        assertEquals(pizza1.getPrice(), pizza2.getPrice(), 
                     "Price should be same regardless of decorator order");
    }
    
    @Test
    void testAllToppingsOnLargePizza() {
        // Pepperoni L: 90,000 + 30,000 = 120,000
        // + Cheese: 10,000
        // + Bacon: 15,000
        // + Mushroom: 8,000
        // Total: 153,000
        Pizza pizza = PizzaFactory.create("Pepperoni", Size.L);
        pizza = new Cheese(pizza);
        pizza = new Bacon(pizza);
        pizza = new Mushroom(pizza);
        
        assertEquals(153_000, pizza.getPrice(), 
                     "Large pepperoni with all toppings should be 153,000");
    }
    
    @Test
    void testDecoratorPreservesOriginalPizza() {
        Pizza original = PizzaFactory.create("Hawaiian", Size.M);
        int originalPrice = original.getPrice();
        
        Pizza decorated = new Cheese(original);
        
        // Original pizza price should be unchanged
        assertEquals(originalPrice, original.getPrice(), 
                     "Original pizza price should not change");
        
        // Decorated pizza should have higher price
        assertTrue(decorated.getPrice() > originalPrice, 
                   "Decorated pizza should cost more");
    }
}






