package com.pizza.ui.controllers;

import com.pizza.app.CartService;
import com.pizza.app.EventBus;
import com.pizza.domain.decorator.Bacon;
import com.pizza.domain.decorator.Cheese;
import com.pizza.domain.decorator.Mushroom;
import com.pizza.domain.factory.PizzaFactory;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Size;
import com.pizza.infra.db.PizzaRepository;
import com.pizza.ui.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the Menu view.
 * Handles pizza selection, topping customization, and adding items to cart.
 */
public class MenuController {

    @FXML
    private ComboBox<String> pizzaTypeCombo;
    @FXML
    private ComboBox<Size> sizeCombo;
    @FXML
    private CheckBox cheeseCheck;
    @FXML
    private CheckBox baconCheck;
    @FXML
    private CheckBox mushroomCheck;
    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private Label cartCountLabel;
    @FXML
    private Label previewLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView pizzaImageView;
    @FXML
    private Label pizzaDescriptionLabel;

    private final CartService cartService = CartService.getInstance();
    private final EventBus eventBus = EventBus.getInstance();
    private final PizzaRepository pizzaRepository = new PizzaRepository();
    private List<PizzaRepository.PizzaData> availablePizzas;

    @FXML
    private void initialize() {
        // Load pizza types from database
        loadPizzasFromDatabase();

        if (availablePizzas != null && !availablePizzas.isEmpty()) {
            pizzaTypeCombo.setValue(availablePizzas.get(0).getName());
            updatePizzaInfo();
        } else {
            pizzaTypeCombo.setValue("Margherita");
        }

        // Initialize sizes
        sizeCombo.getItems().addAll(Size.values());
        sizeCombo.setValue(Size.M);

        // Initialize quantity spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        quantitySpinner.setValueFactory(valueFactory);

        // Update cart count label
        updateCartCount();

        // Subscribe to cart updates
        eventBus.subscribe("CART_UPDATED", data -> updateCartCount());

        // Add listeners for preview
        pizzaTypeCombo.setOnAction(e -> {
            updatePizzaInfo();
            updatePreview();
        });
        sizeCombo.setOnAction(e -> updatePreview());
        cheeseCheck.setOnAction(e -> updatePreview());
        baconCheck.setOnAction(e -> updatePreview());
        mushroomCheck.setOnAction(e -> updatePreview());

        // Initial preview
        updatePreview();
    }

    /**
     * Load available pizzas from database.
     */
    private void loadPizzasFromDatabase() {
        try {
            availablePizzas = pizzaRepository.getAllPizzas();
            pizzaTypeCombo.getItems().clear();
            for (PizzaRepository.PizzaData pizzaData : availablePizzas) {
                pizzaTypeCombo.getItems().add(pizzaData.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to hardcoded types
            pizzaTypeCombo.getItems().addAll("Margherita", "Pepperoni", "Hawaiian", "Seafood", "Veggie Supreme");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Không thể load pizza từ database");
            alert.setContentText("Sử dụng danh sách mặc định.");
            alert.showAndWait();
        }
    }

    /**
     * Update pizza image and description when pizza type changes.
     */
    private void updatePizzaInfo() {
        String selectedPizza = pizzaTypeCombo.getValue();
        if (selectedPizza == null || availablePizzas == null)
            return;

        for (PizzaRepository.PizzaData pizzaData : availablePizzas) {
            if (pizzaData.getName().equals(selectedPizza)) {
                // Load image
                if (pizzaImageView != null) {
                    loadPizzaImage(pizzaData.getImageUrl());
                }

                // Update description
                if (pizzaDescriptionLabel != null) {
                    pizzaDescriptionLabel.setText(pizzaData.getDescription());
                }
                break;
            }
        }
    }

    /**
     * Load pizza image from URL or resources with fallback to placeholder.
     */
    private void loadPizzaImage(String imagePath) {
        try {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // Load from URL (Internet)
                Image image = new Image(imagePath, true); // backgroundLoading = true
                pizzaImageView.setImage(image);
                System.out.println("✅ Đang tải ảnh từ: " + imagePath);
            } else {
                // Try to load from resources (local files)
                InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);

                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    pizzaImageView.setImage(image);
                    System.out.println("✅ Đã load ảnh từ resources: " + imagePath);
                } else {
                    // Fallback: Try alternative extensions
                    String[] extensions = { ".png", ".jpg", ".jpeg" };
                    boolean loaded = false;

                    for (String ext : extensions) {
                        String altPath = imagePath.replace(".png", ext).replace(".jpg", ext).replace(".jpeg", ext);
                        imageStream = getClass().getResourceAsStream("/" + altPath);

                        if (imageStream != null) {
                            Image image = new Image(imageStream);
                            pizzaImageView.setImage(image);
                            loaded = true;
                            System.out.println("✅ Đã load ảnh từ resources (alt): " + altPath);
                            break;
                        }
                    }

                    if (!loaded) {
                        // Use placeholder or default image
                        loadDefaultPlaceholder();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load ảnh: " + e.getMessage());
            e.printStackTrace();
            loadDefaultPlaceholder();
        }
    }

    /**
     * Load default placeholder image when pizza image not found.
     */
    private void loadDefaultPlaceholder() {
        try {
            // Try to create a simple colored circle or use emoji as fallback
            // For now, we'll just show nothing or you can add a default pizza image
            System.out.println("⚠️ Không tìm thấy ảnh pizza. Vui lòng thêm ảnh vào thư mục resources/images/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddToCart() {
        int quantity = quantitySpinner.getValue();

        for (int i = 0; i < quantity; i++) {
            Pizza pizza = createPizza();
            cartService.addItem(pizza);
        }

        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Đã thêm " + quantity + " pizza vào giỏ hàng! 🎉");
        alert.showAndWait();

        // Reset form
        resetForm();
    }

    @FXML
    private void handleViewCart() {
        try {
            MainApp.showCart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToHome() {
        try {
            MainApp.showHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pizza createPizza() {
        String type = pizzaTypeCombo.getValue();
        Size size = sizeCombo.getValue();

        Pizza pizza = PizzaFactory.create(type, size);

        // Apply decorators for selected toppings
        if (cheeseCheck.isSelected()) {
            pizza = new Cheese(pizza);
        }
        if (baconCheck.isSelected()) {
            pizza = new Bacon(pizza);
        }
        if (mushroomCheck.isSelected()) {
            pizza = new Mushroom(pizza);
        }

        return pizza;
    }

    private void updatePreview() {
        Pizza pizza = createPizza();
        String desc = pizza.getDescription()
                .replace("Small", "Nhỏ")
                .replace("Medium", "Vừa")
                .replace("Large", "Lớn");
        previewLabel.setText(desc);
        priceLabel.setText(String.format("%,dđ", pizza.getPrice()));
    }

    private void updateCartCount() {
        javafx.application.Platform
                .runLater(() -> cartCountLabel.setText("Giỏ hàng: " + cartService.getItemCount() + " món"));
    }

    private void resetForm() {
        cheeseCheck.setSelected(false);
        baconCheck.setSelected(false);
        mushroomCheck.setSelected(false);
        quantitySpinner.getValueFactory().setValue(1);
        updatePreview();
    }
}
