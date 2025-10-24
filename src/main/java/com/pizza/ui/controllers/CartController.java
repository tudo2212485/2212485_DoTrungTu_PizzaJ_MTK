package com.pizza.ui.controllers;

import com.pizza.app.CartService;
import com.pizza.app.EventBus;
import com.pizza.domain.decorator.ToppingDecorator;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.strategy.ExpressShipping;
import com.pizza.domain.strategy.StandardShipping;
import com.pizza.infra.db.OrderRepository;
import com.pizza.ui.MainApp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Cart/Checkout view.
 */
public class CartController {

    @FXML
    private TableView<Pizza> cartTable;
    @FXML
    private TableColumn<Pizza, String> itemColumn;
    @FXML
    private TableColumn<Pizza, String> toppingsColumn;
    @FXML
    private TableColumn<Pizza, String> priceColumn;

    @FXML
    private RadioButton standardShippingRadio;
    @FXML
    private RadioButton expressShippingRadio;

    @FXML
    private Label subtotalLabel;
    @FXML
    private Label shippingLabel;
    @FXML
    private Label totalLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressArea;

    @FXML
    private Button placeOrderButton;

    private final CartService cartService = CartService.getInstance();
    private final EventBus eventBus = EventBus.getInstance();
    private final OrderRepository orderRepository = new OrderRepository();

    @FXML
    private void initialize() {
        // Setup table columns
        itemColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        toppingsColumn.setCellValueFactory(data -> {
            Pizza pizza = data.getValue();
            String toppings = pizza instanceof ToppingDecorator
                    ? ((ToppingDecorator) pizza).getToppings()
                    : "-";
            return new SimpleStringProperty(toppings);
        });

        priceColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.format("%,dđ", data.getValue().getPrice())));

        // Setup shipping radio buttons
        ToggleGroup shippingGroup = new ToggleGroup();
        standardShippingRadio.setToggleGroup(shippingGroup);
        expressShippingRadio.setToggleGroup(shippingGroup);
        standardShippingRadio.setSelected(true);

        standardShippingRadio.setOnAction(e -> {
            cartService.setShippingStrategy(new StandardShipping());
            updateTotals();
        });

        expressShippingRadio.setOnAction(e -> {
            cartService.setShippingStrategy(new ExpressShipping());
            updateTotals();
        });

        // Load cart items
        loadCartItems();

        // Update totals
        updateTotals();

        // Subscribe to cart updates
        eventBus.subscribe("CART_UPDATED", data -> {
            loadCartItems();
            updateTotals();
        });
    }

    @FXML
    private void handleRemoveItem() {
        int selectedIndex = cartTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cartService.removeItem(selectedIndex);
        } else {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn món", "Vui lòng chọn món cần xóa.");
        }
    }

    @FXML
    private void handleEditItem() {
        Pizza selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn món", "Vui lòng chọn món cần sửa.");
            return;
        }

        int index = cartTable.getSelectionModel().getSelectedIndex();
        showEditDialog(selected, index);
    }

    /**
     * Show dialog to edit pizza toppings.
     */
    private void showEditDialog(Pizza originalPizza, int index) {
        // Create dialog
        Dialog<Pizza> dialog = new Dialog<>();
        dialog.setTitle("✏️ Sửa Pizza");
        dialog.setHeaderText("Chỉnh sửa topping");

        // Create content
        VBox content = new VBox(12);
        content.setPadding(new javafx.geometry.Insets(15));
        content.setStyle("-fx-background-color: white;");

        Label infoLabel = new Label("🍕 " + getBasePizzaName(originalPizza));
        infoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        Label priceLabel = new Label(String.format("Giá hiện tại: %,dđ", originalPizza.getPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #424242;");

        Separator sep = new Separator();

        Label toppingLabel = new Label("🧀 Chọn topping:");
        toppingLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        // Topping checkboxes
        CheckBox cheeseCheck = new CheckBox("Phô mai thêm (+10,000đ)");
        CheckBox baconCheck = new CheckBox("Thịt xông khói (+15,000đ)");
        CheckBox mushroomCheck = new CheckBox("Nấm (+8,000đ)");

        cheeseCheck.setStyle("-fx-font-size: 13px;");
        baconCheck.setStyle("-fx-font-size: 13px;");
        mushroomCheck.setStyle("-fx-font-size: 13px;");

        // Parse current toppings
        String desc = originalPizza.getDescription().toLowerCase();
        cheeseCheck.setSelected(desc.contains("extra cheese") || desc.contains("phô mai"));
        baconCheck.setSelected(desc.contains("bacon") || desc.contains("thịt"));
        mushroomCheck.setSelected(desc.contains("mushroom") || desc.contains("nấm"));

        content.getChildren().addAll(
                infoLabel,
                priceLabel,
                sep,
                toppingLabel,
                cheeseCheck,
                baconCheck,
                mushroomCheck);

        dialog.getDialogPane().setContent(content);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("💾 Lưu", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("❌ Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Style buttons
        dialog.getDialogPane().lookupButton(saveButtonType).setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        // Result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                Pizza newPizza = buildPizzaFromOriginal(originalPizza,
                        cheeseCheck.isSelected(),
                        baconCheck.isSelected(),
                        mushroomCheck.isSelected());
                return newPizza;
            }
            return null;
        });

        // Show dialog and handle result
        dialog.showAndWait().ifPresent(newPizza -> {
            cartService.replaceItem(index, newPizza);
            showAlert(Alert.AlertType.INFORMATION, "Thành công ✅",
                    "Đã cập nhật pizza!\n\n" +
                            "Giá mới: " + String.format("%,dđ", newPizza.getPrice()));
        });
    }

    /**
     * Get base pizza name without topping decorators.
     */
    private String getBasePizzaName(Pizza pizza) {
        Pizza base = getBasePizza(pizza);
        String desc = base.getDescription();
        // Extract just the pizza name (before size)
        if (desc.contains("(")) {
            return desc.substring(0, desc.indexOf("(")).trim();
        }
        return desc;
    }

    /**
     * Build a new pizza based on original but with new toppings.
     */
    private Pizza buildPizzaFromOriginal(Pizza original, boolean addCheese, boolean addBacon, boolean addMushroom) {
        // Get the base pizza (without toppings)
        Pizza base = getBasePizza(original);

        // Re-apply selected toppings
        Pizza pizza = base;
        if (addCheese) {
            pizza = new com.pizza.domain.decorator.Cheese(pizza);
        }
        if (addBacon) {
            pizza = new com.pizza.domain.decorator.Bacon(pizza);
        }
        if (addMushroom) {
            pizza = new com.pizza.domain.decorator.Mushroom(pizza);
        }

        return pizza;
    }

    /**
     * Extract base pizza from decorated pizza.
     */
    private Pizza getBasePizza(Pizza pizza) {
        Pizza current = pizza;
        while (current instanceof ToppingDecorator) {
            current = ((ToppingDecorator) current).getWrappedPizza();
        }
        return current;
    }

    @FXML
    private void handlePlaceOrder() {
        // Validate inputs
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập họ tên của bạn.");
            return;
        }
        if (phoneField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập số điện thoại.");
            return;
        }
        if (addressArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập địa chỉ giao hàng.");
            return;
        }
        if (cartService.getItemCount() == 0) {
            showAlert(Alert.AlertType.ERROR, "Giỏ hàng trống", "Giỏ hàng trống. Vui lòng thêm món trước.");
            return;
        }

        try {
            // Save order to database
            int orderId = orderRepository.saveOrder(
                    nameField.getText().trim(),
                    phoneField.getText().trim(),
                    addressArea.getText().trim(),
                    cartService.getShippingStrategy().getName(),
                    cartService.getItems(),
                    cartService.getSubtotal(),
                    cartService.getShippingFee(),
                    cartService.getTotal());

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đặt hàng thành công");
            alert.setHeaderText("🎉 Đơn hàng #" + orderId + " đã được đặt!");
            alert.setContentText(String.format(
                    "Tổng tiền: %,dđ\n\nCảm ơn bạn đã đặt hàng! 🍕\nChúng tôi sẽ giao hàng sớm nhất có thể.",
                    cartService.getTotal()));
            alert.showAndWait();

            // Clear cart and return to home
            cartService.clear();
            MainApp.showHome();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu",
                    "Không thể lưu đơn hàng: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContinueShopping() {
        try {
            MainApp.showMenu();
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

    private void loadCartItems() {
        javafx.application.Platform.runLater(() -> {
            ObservableList<Pizza> items = FXCollections.observableArrayList(cartService.getItems());
            cartTable.setItems(items);
        });
    }

    private void updateTotals() {
        javafx.application.Platform.runLater(() -> {
            subtotalLabel.setText(String.format("%,dđ", cartService.getSubtotal()));
            shippingLabel.setText(String.format("%,dđ", cartService.getShippingFee()));
            totalLabel.setText(String.format("%,dđ", cartService.getTotal()));
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
