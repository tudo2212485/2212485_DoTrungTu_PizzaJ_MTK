package com.pizza.infra.db;

import com.pizza.domain.decorator.ToppingDecorator;
import com.pizza.domain.pizza.Pizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Repository for persisting orders to SQLite database.
 * Follows Repository pattern for data access abstraction.
 */
public class OrderRepository {

    /**
     * Save a POS order to the database.
     * 
     * SOLID PRINCIPLES APPLIED:
     * - Single Responsibility: Only handles order persistence
     * - Dependency Inversion: Uses abstraction for database connection
     * 
     * @param customerName  Customer's name (optional for walk-in)
     * @param paymentMethod Payment method used (Cash/Card/E-Wallet)
     * @param items         List of pizza items
     * @param total         Total amount
     * @return The ID of the created order
     */
    public int saveOrder(String customerName, String paymentMethod,
            List<Pizza> items, int total) throws SQLException {

        // ✅ CLEAN: Dùng schema mới chỉ với 4 cột cần thiết
        try (Connection conn = SQLiteConnection.getConnection()) {

            // Insert order header - CLEAN SCHEMA
            String orderSql = """
                    INSERT INTO orders (customer_name, payment_method, total)
                    VALUES (?, ?, ?)
                    """;

            int orderId;
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, customerName != null && !customerName.trim().isEmpty()
                        ? customerName
                        : "Khách vãng lai");
                pstmt.setString(2, paymentMethod);
                pstmt.setInt(3, total);

                pstmt.executeUpdate();

                // Get generated order ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get order ID");
                    }
                }
            }

            // Insert order items
            String itemSql = """
                    INSERT INTO order_items (order_id, pizza_name, size, toppings, price)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(itemSql)) {
                for (Pizza pizza : items) {
                    pstmt.setInt(1, orderId);

                    // ✅ FIX: Get base pizza name (unwrap decorators)
                    String pizzaName = getBasePizzaName(pizza);
                    pstmt.setString(2, pizzaName);
                    pstmt.setString(3, pizza.getSize().name());

                    // ✅ FIX: Extract ALL toppings properly
                    String toppings = extractAllToppings(pizza);
                    pstmt.setString(4, toppings.isEmpty() ? null : toppings);
                    pstmt.setInt(5, pizza.getPrice());

                    pstmt.executeUpdate();
                }
            }

            return orderId;

        } // ✅ Connection auto-closed here
    }

    /**
     * Extract base pizza name by unwrapping all decorators.
     */
    private String getBasePizzaName(Pizza pizza) {
        Pizza current = pizza;
        while (current instanceof ToppingDecorator) {
            current = ((ToppingDecorator) current).getWrappedPizza();
        }
        return current.getName();
    }

    /**
     * Extract ALL toppings from decorated pizza.
     * Handles multiple layers of decorators correctly.
     */
    private String extractAllToppings(Pizza pizza) {
        if (!(pizza instanceof ToppingDecorator)) {
            return "";
        }

        // Use getToppings() method from ToppingDecorator
        return ((ToppingDecorator) pizza).getToppings();
    }
}
