package com.pizza.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX Application class.
 * Entry point for the Pizza Order Application.
 */
public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("🍕 POS Pizza - Point of Sale");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(750);

        // Start with POS screen (single-screen interface)
        showPos();

        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Navigate to Home view.
     */
    public static void showHome() throws IOException {
        loadView("views/home.fxml", "🍕 Ứng Dụng Đặt Pizza - Trang Chủ");
    }

    /**
     * Navigate to Menu view.
     */
    public static void showMenu() throws IOException {
        loadView("views/menu.fxml", "🍕 Ứng Dụng Đặt Pizza - Thực Đơn");
    }

    /**
     * Navigate to Cart view.
     */
    public static void showCart() throws IOException {
        loadView("views/cart.fxml", "🍕 Ứng Dụng Đặt Pizza - Giỏ Hàng");
    }

    /**
     * Navigate to POS (Point of Sale) view - Single screen interface.
     */
    public static void showPos() throws IOException {
        loadView("views/pos.fxml", "🍕 POS Pizza - Point of Sale System");
    }

    /**
     * Load a view from FXML file.
     */
    private static void loadView(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/" + fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(MainApp.class.getResource("/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
