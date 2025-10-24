package com.pizza.infra.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite database connection manager.
 * Handles database initialization and connection management.
 */
public class SQLiteConnection {
    private static final String DB_URL = "jdbc:sqlite:pizza_orders.db";
    private static Connection connection;

    /**
     * Get database connection. Creates database and tables if they don't exist.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        }
        return connection;
    }

    /**
     * Initialize database schema.
     * Creates pizzas, orders and order_items tables if they don't exist.
     */
    private static void initializeDatabase() throws SQLException {
        // Bảng danh sách pizza
        String createPizzasTable = """
                CREATE TABLE IF NOT EXISTS pizzas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    description TEXT,
                    base_price INTEGER NOT NULL,
                    image_url TEXT,
                    is_active INTEGER DEFAULT 1
                )
                """;

        String createOrdersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_name TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    address TEXT NOT NULL,
                    shipping_type TEXT NOT NULL,
                    subtotal INTEGER NOT NULL,
                    shipping_fee INTEGER NOT NULL,
                    total INTEGER NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id INTEGER NOT NULL,
                    pizza_name TEXT NOT NULL,
                    size TEXT NOT NULL,
                    toppings TEXT,
                    price INTEGER NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id)
                )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPizzasTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);

            // Insert dữ liệu mẫu 5 loại pizza
            insertSamplePizzas();
        }
    }

    /**
     * Insert dữ liệu mẫu 5 loại pizza vào database.
     */
    private static void insertSamplePizzas() throws SQLException {
        String checkData = "SELECT COUNT(*) FROM pizzas";
        try (Statement stmt = connection.createStatement();
                var rs = stmt.executeQuery(checkData)) {
            if (rs.getInt(1) > 0) {
                return; // Đã có dữ liệu, không cần insert
            }
        }

        String insertPizza = """
                INSERT INTO pizzas (name, description, base_price, image_url) VALUES (?, ?, ?, ?)
                """;

        try (var pstmt = connection.prepareStatement(insertPizza)) {
            // 1. Margherita - URL ảnh từ Internet
            pstmt.setString(1, "Margherita");
            pstmt.setString(2, "Pizza cổ điển với sốt cà chua, phô mai Mozzarella và lá húng quế tươi");
            pstmt.setInt(3, 60_000);
            pstmt.setString(4, "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400");
            pstmt.executeUpdate();

            // 2. Pepperoni
            pstmt.setString(1, "Pepperoni");
            pstmt.setString(2, "Pizza với xúc xích Pepperoni cay nồng và phô mai tan chảy");
            pstmt.setInt(3, 90_000);
            pstmt.setString(4, "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=400");
            pstmt.executeUpdate();

            // 3. Hawaiian
            pstmt.setString(1, "Hawaiian");
            pstmt.setString(2, "Pizza độc đáo với thịt nguội và dứa ngọt ngào");
            pstmt.setInt(3, 80_000);
            pstmt.setString(4, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400");
            pstmt.executeUpdate();

            // 4. Seafood
            pstmt.setString(1, "Seafood");
            pstmt.setString(2, "Pizza hải sản tươi ngon với tôm, mực và nghêu");
            pstmt.setInt(3, 120_000);
            pstmt.setString(4, "https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f?w=400");
            pstmt.executeUpdate();

            // 5. Veggie Supreme
            pstmt.setString(1, "Veggie Supreme");
            pstmt.setString(2, "Pizza chay đầy đủ dinh dưỡng với nhiều loại rau củ");
            pstmt.setInt(3, 70_000);
            pstmt.setString(4, "https://images.unsplash.com/photo-1511689660979-10d2b1aada49?w=400");
            pstmt.executeUpdate();
        }
    }

    /**
     * Close database connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
