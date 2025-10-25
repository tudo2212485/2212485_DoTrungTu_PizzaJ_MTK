package com.pizza.infra.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for accessing pizza data from SQLite database.
 */
public class PizzaRepository {

    /**
     * Model class representing pizza data from database.
     */
    public static class PizzaData {
        private final int id;
        private final String name;
        private final String description;
        private final int basePrice;
        private final String imageUrl;

        public PizzaData(int id, String name, String description, int basePrice, String imageUrl) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.basePrice = basePrice;
            this.imageUrl = imageUrl;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getBasePrice() {
            return basePrice;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    /**
     * Get all active pizzas from database.
     */
    public List<PizzaData> getAllPizzas() throws SQLException {
        List<PizzaData> pizzas = new ArrayList<>();
        String query = "SELECT id, name, description, base_price, image_url FROM pizzas WHERE is_active = 1 ORDER BY name";

        // ✅ FIX: Auto-close connection với try-with-resources
        try (Connection conn = SQLiteConnection.getConnection();
                var stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                pizzas.add(new PizzaData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("base_price"),
                        rs.getString("image_url")));
            }
        } // ✅ Connection auto-closed

        return pizzas;
    }

    /**
     * Get pizza by name.
     */
    public PizzaData getPizzaByName(String name) throws SQLException {
        String query = "SELECT id, name, description, base_price, image_url FROM pizzas WHERE name = ? AND is_active = 1";

        // ✅ FIX: Auto-close connection với try-with-resources
        try (Connection conn = SQLiteConnection.getConnection();
                var pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new PizzaData(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("base_price"),
                            rs.getString("image_url"));
                }
            }
        } // ✅ Connection auto-closed

        return null;
    }
}
