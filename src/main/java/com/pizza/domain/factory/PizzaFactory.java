package com.pizza.domain.factory;

import com.pizza.domain.pizza.Hawaiian;
import com.pizza.domain.pizza.Margherita;
import com.pizza.domain.pizza.Pepperoni;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Seafood;
import com.pizza.domain.pizza.Size;
import com.pizza.domain.pizza.VeggieSupreme;

/**
 * Factory for creating Pizza objects.
 * DESIGN PATTERN: Factory Pattern
 * 
 * Encapsulates the object creation logic, allowing clients to create
 * pizza objects without knowing the concrete classes. This promotes
 * loose coupling and follows the Open/Closed Principle.
 */
public class PizzaFactory {

    /**
     * Creates a pizza based on type and size.
     * 
     * @param type The type of pizza (Margherita, Pepperoni, Hawaiian, Seafood,
     *             Veggie Supreme)
     * @param size The size of the pizza (S, M, L)
     * @return A new Pizza instance
     * @throws IllegalArgumentException if pizza type is unknown
     */
    public static Pizza create(String type, Size size) {
        if (type == null || size == null) {
            throw new IllegalArgumentException("Pizza type and size cannot be null");
        }

        return switch (type.toLowerCase().replace(" ", "")) {
            case "margherita" -> new Margherita(size);
            case "pepperoni" -> new Pepperoni(size);
            case "hawaiian" -> new Hawaiian(size);
            case "seafood" -> new Seafood(size);
            case "veggiesupreme" -> new VeggieSupreme(size);
            default -> throw new IllegalArgumentException("Unknown pizza type: " + type);
        };
    }

    /**
     * Get all available pizza types.
     * 
     * @deprecated Use PizzaRepository.getAllPizzas() instead to load from database
     */
    @Deprecated
    public static String[] getAvailableTypes() {
        return new String[] { "Margherita", "Pepperoni", "Hawaiian", "Seafood", "Veggie Supreme" };
    }
}
