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
     * Save an order to the database.
     * 
     * @param customerName Customer's name
     * @param phone Customer's phone number
     * @param address Delivery address
     * @param shippingType Type of shipping (Standard/Express)
     * @param items List of pizza items
     * @param subtotal Order subtotal
     * @param shippingFee Shipping fee
     * @param total Total amount
     * @return The ID of the created order
     */
    public int saveOrder(String customerName, String phone, String address, 
                         String shippingType, List<Pizza> items, 
                         int subtotal, int shippingFee, int total) throws SQLException {
        
        Connection conn = SQLiteConnection.getConnection();
        
        // Insert order header
        String orderSql = """
            INSERT INTO orders (customer_name, phone, address, shipping_type, 
                               subtotal, shipping_fee, total)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        int orderId;
        try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setString(4, shippingType);
            pstmt.setInt(5, subtotal);
            pstmt.setInt(6, shippingFee);
            pstmt.setInt(7, total);
            
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
                pstmt.setString(2, pizza.getName());
                pstmt.setString(3, pizza.getSize().name());
                
                // Extract toppings if pizza is decorated
                String toppings = "";
                if (pizza instanceof ToppingDecorator) {
                    toppings = ((ToppingDecorator) pizza).getToppings();
                }
                pstmt.setString(4, toppings.isEmpty() ? null : toppings);
                pstmt.setInt(5, pizza.getPrice());
                
                pstmt.executeUpdate();
            }
        }
        
        return orderId;
    }
}









