package com.pizza;

import com.pizza.domain.strategy.ExpressShipping;
import com.pizza.domain.strategy.ShippingStrategy;
import com.pizza.domain.strategy.StandardShipping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Shipping Strategy pattern implementations.
 * Tests boundary conditions and strategy-specific logic.
 */
class StrategyTest {
    
    @Test
    void testStandardShippingBelowThreshold() {
        ShippingStrategy strategy = new StandardShipping();
        
        int fee1 = strategy.calculateFee(50_000);
        assertEquals(20_000, fee1, "Fee should be 20,000 for 50k");
        
        int fee2 = strategy.calculateFee(199_999);
        assertEquals(20_000, fee2, "Fee should be 20,000 for 199,999");
    }
    
    @Test
    void testStandardShippingAtThreshold() {
        // Boundary test: exactly at 200,000
        ShippingStrategy strategy = new StandardShipping();
        
        int fee = strategy.calculateFee(200_000);
        assertEquals(0, fee, "Fee should be 0 at exactly 200,000");
    }
    
    @Test
    void testStandardShippingAboveThreshold() {
        ShippingStrategy strategy = new StandardShipping();
        
        int fee1 = strategy.calculateFee(200_001);
        assertEquals(0, fee1, "Fee should be 0 for 200,001");
        
        int fee2 = strategy.calculateFee(1_000_000);
        assertEquals(0, fee2, "Fee should be 0 for 1,000,000");
    }
    
    @Test
    void testExpressShippingIsFlat() {
        ShippingStrategy strategy = new ExpressShipping();
        
        // Express should be 40,000 regardless of subtotal
        assertEquals(40_000, strategy.calculateFee(0));
        assertEquals(40_000, strategy.calculateFee(50_000));
        assertEquals(40_000, strategy.calculateFee(200_000));
        assertEquals(40_000, strategy.calculateFee(1_000_000));
    }
    
    @Test
    void testStrategyNames() {
        ShippingStrategy standard = new StandardShipping();
        assertEquals("Standard Shipping", standard.getName());
        
        ShippingStrategy express = new ExpressShipping();
        assertEquals("Express Shipping", express.getName());
    }
    
    @Test
    void testStrategyPolymorphism() {
        // Test that both strategies can be used polymorphically
        ShippingStrategy[] strategies = {
            new StandardShipping(),
            new ExpressShipping()
        };
        
        for (ShippingStrategy strategy : strategies) {
            assertNotNull(strategy.getName());
            assertTrue(strategy.calculateFee(100_000) >= 0);
        }
    }
}






