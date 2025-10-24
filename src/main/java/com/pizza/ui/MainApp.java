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
        primaryStage.setTitle("🍕 Ứng Dụng Đặt Pizza");
        primaryStage.setWidth(950);
        primaryStage.setHeight(750);
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);

        showHome();

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
