package com.pizza.ui.controllers;

import com.pizza.app.CartService;
import com.pizza.app.EventBus;
import com.pizza.domain.decorator.Bacon;
import com.pizza.domain.decorator.Cheese;
import com.pizza.domain.decorator.Mushroom;
import com.pizza.domain.decorator.ToppingDecorator;
import com.pizza.domain.factory.PizzaFactory;
import com.pizza.domain.pizza.Pizza;
import com.pizza.domain.pizza.Size;
import com.pizza.domain.strategy.CashPayment;
import com.pizza.domain.strategy.CardPayment;
import com.pizza.domain.strategy.EWalletPayment;
import com.pizza.infra.db.OrderRepository;
import com.pizza.infra.db.PizzaRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.ButtonBar;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Single-Screen POS Interface.
 * 
 * SOLID PRINCIPLES APPLIED:
 * - Single Responsibility: Manages POS UI and user interactions
 * - Dependency Inversion: Depends on abstractions (CartService, EventBus)
 * - Open/Closed: Can extend without modifying core logic
 */
public class PosController {

    @FXML
    private GridPane pizzaGrid;
    @FXML
    private VBox orderItemsBox;

    // Size selection
    @FXML
    private ToggleButton sizeSmall;
    @FXML
    private ToggleButton sizeMedium;
    @FXML
    private ToggleButton sizeLarge;

    // Topping selection
    @FXML
    private CheckBox cheeseCheck;
    @FXML
    private CheckBox baconCheck;
    @FXML
    private CheckBox mushroomCheck;

    // Payment method
    @FXML
    private ToggleButton cashPaymentBtn;
    @FXML
    private ToggleButton cardPaymentBtn;
    @FXML
    private ToggleButton ewalletPaymentBtn;

    // Labels
    @FXML
    private Label orderNumberLabel;
    @FXML
    private Label cashierLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label totalLabel;

    // Input
    @FXML
    private TextField customerNameField;
    @FXML
    private Button addToOrderButton;

    private final CartService cartService = CartService.getInstance();
    private final EventBus eventBus = EventBus.getInstance();
    private final OrderRepository orderRepository = new OrderRepository();
    private final PizzaRepository pizzaRepository = new PizzaRepository();

    private ToggleGroup sizeGroup;
    private ToggleGroup paymentGroup;
    private String selectedPizzaType = null;
    private int selectedItemIndex = -1;
    private int orderCounter = 1;

    @FXML
    private void initialize() {
        setupToggleGroups();
        loadPizzaMenu();
        setupDateTime();
        updateOrderDisplay();

        // Subscribe to cart updates (Observer pattern)
        eventBus.subscribe("CART_UPDATED", data -> {
            updateOrderDisplay();
        });

        // Set default payment strategy
        cartService.setPaymentStrategy(new CashPayment());
    }

    private void setupToggleGroups() {
        // Size toggle group
        sizeGroup = new ToggleGroup();
        sizeSmall.setToggleGroup(sizeGroup);
        sizeMedium.setToggleGroup(sizeGroup);
        sizeLarge.setToggleGroup(sizeGroup);
        sizeMedium.setSelected(true); // Default to Medium

        // Payment toggle group
        paymentGroup = new ToggleGroup();
        cashPaymentBtn.setToggleGroup(paymentGroup);
        cardPaymentBtn.setToggleGroup(paymentGroup);
        ewalletPaymentBtn.setToggleGroup(paymentGroup);

        // Prevent deselecting all payment methods
        paymentGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                paymentGroup.selectToggle(oldToggle);
            }
        });

        cashPaymentBtn.setSelected(true); // Default to Cash

        // Payment method change listeners
        cashPaymentBtn.setOnAction(e -> cartService.setPaymentStrategy(new CashPayment()));
        cardPaymentBtn.setOnAction(e -> cartService.setPaymentStrategy(new CardPayment()));
        ewalletPaymentBtn.setOnAction(e -> cartService.setPaymentStrategy(new EWalletPayment()));
    }

    private void loadPizzaMenu() {
        try {
            List<PizzaRepository.PizzaData> pizzas = pizzaRepository.getAllPizzas();

            int col = 0;
            int row = 0;
            int maxCols = 3; // 3 columns grid

            for (PizzaRepository.PizzaData pizzaData : pizzas) {
                Button pizzaButton = createPizzaButton(pizzaData);
                pizzaGrid.add(pizzaButton, col, row);

                col++;
                if (col >= maxCols) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải menu: " + e.getMessage());
        }
    }

    private Button createPizzaButton(PizzaRepository.PizzaData pizzaData) {
        Button button = new Button();
        button.setPrefSize(140, 140);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.getStyleClass().add("pizza-card-button");

        // Create VBox layout for image + text
        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);

        // Pizza image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        try {
            Image image = new Image(pizzaData.getImageUrl(), true); // Load async
            imageView.setImage(image);
        } catch (Exception e) {
            // If image fails, show emoji placeholder
            Label placeholder = new Label("🍕");
            placeholder.setStyle("-fx-font-size: 50px;");
            content.getChildren().add(placeholder);
        }

        if (imageView.getImage() != null) {
            content.getChildren().add(imageView);
        }

        // Pizza name
        Label nameLabel = new Label(pizzaData.getName());
        nameLabel.setFont(Font.font("System Bold", 13));
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(130);

        // Pizza price
        Label priceLabel = new Label(String.format("%,dđ", pizzaData.getBasePrice()));
        priceLabel.setFont(Font.font("System", 12));
        priceLabel.setStyle("-fx-text-fill: #e74c3c;");

        content.getChildren().addAll(nameLabel, priceLabel);
        button.setGraphic(content);

        // Click handler
        button.setOnAction(e -> {
            // Use name as type (remove spaces for Factory)
            selectedPizzaType = pizzaData.getName().replace(" ", "");
            // Highlight selected
            pizzaGrid.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    node.getStyleClass().remove("selected");
                }
            });
            button.getStyleClass().add("selected");
        });

        return button;
    }

    private void setupDateTime() {
        // Update date/time every second
        Thread timeThread = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    dateTimeLabel.setText(now.format(formatter));
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timeThread.setDaemon(true);
        timeThread.start();
    }

    @FXML
    private void handleAddToOrder() {
        if (selectedPizzaType == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn Pizza",
                    "Vui lòng chọn một loại pizza từ menu.");
            return;
        }

        try {
            // Get selected size
            Size size = getSelectedSize();

            // Create pizza using Factory Pattern
            Pizza pizza = PizzaFactory.create(selectedPizzaType, size);

            // Add toppings using Decorator Pattern
            if (cheeseCheck.isSelected()) {
                pizza = new Cheese(pizza);
            }
            if (baconCheck.isSelected()) {
                pizza = new Bacon(pizza);
            }
            if (mushroomCheck.isSelected()) {
                pizza = new Mushroom(pizza);
            }

            // Add to cart
            cartService.addItem(pizza);

            // Reset selection
            resetSelection();

            // Show feedback
            showToast("✅ Đã thêm vào đơn!");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm món: " + e.getMessage());
        }
    }

    private Size getSelectedSize() {
        if (sizeSmall.isSelected())
            return Size.S;
        if (sizeLarge.isSelected())
            return Size.L;
        return Size.M; // Default
    }

    private void resetSelection() {
        // Clear pizza selection
        pizzaGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.getStyleClass().remove("selected");
            }
        });
        selectedPizzaType = null;

        // Reset toppings
        cheeseCheck.setSelected(false);
        baconCheck.setSelected(false);
        mushroomCheck.setSelected(false);

        // Reset size to Medium
        sizeMedium.setSelected(true);
    }

    @FXML
    private void handleRemoveItem() {
        if (selectedItemIndex >= 0 && selectedItemIndex < cartService.getItemCount()) {
            cartService.removeItem(selectedItemIndex);
            selectedItemIndex = -1;
        } else {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn món",
                    "Vui lòng chọn món cần xóa từ danh sách đơn hàng.");
        }
    }

    @FXML
    private void handleEditItem() {
        if (selectedItemIndex < 0 || selectedItemIndex >= cartService.getItemCount()) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn món",
                    "Vui lòng chọn món cần sửa từ danh sách đơn hàng.");
            return;
        }

        Pizza currentPizza = cartService.getItems().get(selectedItemIndex);

        // Create edit dialog
        Dialog<Pizza> dialog = new Dialog<>();
        dialog.setTitle("✏️ Sửa Món");
        dialog.setHeaderText("Chỉnh sửa: " + getBasePizza(currentPizza).getName());

        // Set button types
        ButtonType saveButtonType = new ButtonType("💾 Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create edit form
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Size selection
        Label sizeLabel = new Label("📏 Kích thước:");
        sizeLabel.setFont(Font.font("System Bold", 14));

        ToggleGroup editSizeGroup = new ToggleGroup();
        HBox sizeBox = new HBox(10);
        ToggleButton editSmall = new ToggleButton("S (Nhỏ)");
        ToggleButton editMedium = new ToggleButton("M (Vừa)");
        ToggleButton editLarge = new ToggleButton("L (Lớn)");

        editSmall.setToggleGroup(editSizeGroup);
        editMedium.setToggleGroup(editSizeGroup);
        editLarge.setToggleGroup(editSizeGroup);

        editSmall.setPrefWidth(100);
        editMedium.setPrefWidth(100);
        editLarge.setPrefWidth(100);

        // Set current size
        Size currentSize = currentPizza.getSize();
        if (currentSize == Size.S) {
            editSmall.setSelected(true);
        } else if (currentSize == Size.L) {
            editLarge.setSelected(true);
        } else {
            editMedium.setSelected(true);
        }

        sizeBox.getChildren().addAll(editSmall, editMedium, editLarge);

        // Topping selection
        Label toppingLabel = new Label("🧀 Topping:");
        toppingLabel.setFont(Font.font("System Bold", 14));

        CheckBox editCheese = new CheckBox("Phô mai thêm (+10,000đ)");
        CheckBox editBacon = new CheckBox("Thịt xông khói (+15,000đ)");
        CheckBox editMushroom = new CheckBox("Nấm (+8,000đ)");

        // Detect current toppings
        String desc = currentPizza.getDescription();
        editCheese.setSelected(desc.contains("Phô Mai"));
        editBacon.setSelected(desc.contains("Thịt Xông Khói"));
        editMushroom.setSelected(desc.contains("Nấm"));

        content.getChildren().addAll(
                sizeLabel, sizeBox,
                new Separator(),
                toppingLabel, editCheese, editBacon, editMushroom);

        dialog.getDialogPane().setContent(content);

        // Convert result to Pizza
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    // Get selected size
                    Size newSize = Size.M;
                    if (editSmall.isSelected())
                        newSize = Size.S;
                    else if (editLarge.isSelected())
                        newSize = Size.L;

                    // Get base pizza type
                    Pizza basePizza = getBasePizza(currentPizza);
                    String pizzaType = basePizza.getName().replace(" ", "");

                    // Create new pizza with Factory
                    Pizza newPizza = PizzaFactory.create(pizzaType, newSize);

                    // Add toppings with Decorator
                    if (editCheese.isSelected()) {
                        newPizza = new Cheese(newPizza);
                    }
                    if (editBacon.isSelected()) {
                        newPizza = new Bacon(newPizza);
                    }
                    if (editMushroom.isSelected()) {
                        newPizza = new Mushroom(newPizza);
                    }

                    return newPizza;
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        });

        // Show dialog and update cart
        dialog.showAndWait().ifPresent(newPizza -> {
            if (newPizza != null) {
                cartService.replaceItem(selectedItemIndex, newPizza);
                selectedItemIndex = -1;
                showToast("✅ Đã cập nhật món!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật món. Vui lòng thử lại.");
            }
        });
    }

    @FXML
    private void handleClearAll() {
        if (cartService.getItemCount() == 0) {
            showAlert(Alert.AlertType.INFORMATION, "Đơn hàng trống",
                    "Không có món nào để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa tất cả món trong đơn?");
        confirm.setContentText("Bạn có chắc chắn muốn xóa tất cả món trong đơn hàng hiện tại?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cartService.clear();
                selectedItemIndex = -1;
                showToast("✅ Đã xóa tất cả món!");
            }
        });
    }

    @FXML
    private void handlePrintReceipt() {
        if (cartService.getItemCount() == 0) {
            showAlert(Alert.AlertType.WARNING, "Đơn hàng trống",
                    "Không có món nào để in hóa đơn.");
            return;
        }

        // Create receipt dialog
        Dialog<Void> receiptDialog = new Dialog<>();
        receiptDialog.setTitle("🖨️ Hóa Đơn");
        receiptDialog.setHeaderText("🍕 POS PIZZA - HÓA ĐƠN BÁN HÀNG");

        // Receipt content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white;");

        // Store info
        Label storeInfo = new Label(
                "════════════════════════════════\n" +
                        "           🍕 POS PIZZA           \n" +
                        "     Địa chỉ: 123 Đường ABC, TP.HCM     \n" +
                        "        ☎ Hotline: 1900-1234        \n" +
                        "════════════════════════════════");
        storeInfo.setFont(Font.font("Monospaced", 12));
        storeInfo.setAlignment(Pos.CENTER);
        storeInfo.setStyle("-fx-text-fill: #2c3e50;");

        // Order info
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Label orderInfo = new Label(
                "Đơn #: " + String.format("%04d", orderCounter) + "\n" +
                        "Thời gian: " + LocalDateTime.now().format(formatter) + "\n" +
                        "Thu ngân: " + cashierLabel.getText() + "\n" +
                        "Khách hàng: "
                        + (customerNameField.getText().isEmpty() ? "Khách vãng lai" : customerNameField.getText()));
        orderInfo.setFont(Font.font("Monospaced", 11));
        orderInfo.setStyle("-fx-text-fill: #34495e;");

        // Items list
        Label itemsHeader = new Label("────────────────────────────────\n" +
                "         CHI TIẾT ĐƠN HÀNG         \n" +
                "────────────────────────────────");
        itemsHeader.setFont(Font.font("Monospaced", 11));
        itemsHeader.setAlignment(Pos.CENTER);

        VBox itemsList = new VBox(5);
        List<Pizza> items = cartService.getItems();
        for (int i = 0; i < items.size(); i++) {
            Pizza pizza = items.get(i);
            Pizza basePizza = getBasePizza(pizza);

            String itemText = String.format("%d. %-25s %,9dđ",
                    i + 1,
                    basePizza.getName() + " (" + getSizeVietnamese(pizza.getSize()) + ")",
                    pizza.getPrice());

            Label itemLabel = new Label(itemText);
            itemLabel.setFont(Font.font("Monospaced", 11));
            itemsList.getChildren().add(itemLabel);

            // Show toppings if any
            if (!pizza.getDescription().equals(basePizza.getDescription())) {
                String toppings = pizza.getDescription()
                        .replace(basePizza.getDescription(), "")
                        .replace(", ", "\n   + ")
                        .trim();
                if (toppings.startsWith(",")) {
                    toppings = toppings.substring(1).trim();
                }
                Label toppingLabel = new Label("   + " + toppings);
                toppingLabel.setFont(Font.font("Monospaced", 10));
                toppingLabel.setStyle("-fx-text-fill: #7f8c8d;");
                itemsList.getChildren().add(toppingLabel);
            }
        }

        // Total section
        int total = cartService.getTotal();
        String paymentMethod = cartService.getPaymentStrategy().getName();

        Label totalSection = new Label(
                "────────────────────────────────\n" +
                        String.format("%-25s %,9dđ", "TỔNG CỘNG:", total) + "\n" +
                        "────────────────────────────────\n" +
                        "Phương thức: " + getPaymentMethodVietnamese(paymentMethod) + "\n" +
                        "════════════════════════════════\n" +
                        "   🙏 CẢM ƠN QUÝ KHÁCH! 🙏   \n" +
                        "      HẸN GẶP LẠI!      \n" +
                        "════════════════════════════════");
        totalSection.setFont(Font.font("Monospaced", 11));
        totalSection.setAlignment(Pos.CENTER);
        totalSection.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        content.getChildren().addAll(
                storeInfo,
                new Separator(),
                orderInfo,
                itemsHeader,
                itemsList,
                totalSection);

        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(450, 500);

        receiptDialog.getDialogPane().setContent(scrollPane);
        receiptDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        receiptDialog.showAndWait();
    }

    /**
     * Show receipt dialog with order details (reusable method).
     * Can be called from preview or after checkout completion.
     */
    private void showReceiptDialog(int orderNum, String customerName, String paymentMethod,
                                   List<Pizza> items, int total) {
        Dialog<Void> receiptDialog = new Dialog<>();
        receiptDialog.setTitle("🖨️ Hóa Đơn");
        receiptDialog.setHeaderText("🍕 POS PIZZA - HÓA ĐƠN BÁN HÀNG");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white;");

        // Store info
        Label storeInfo = new Label(
                "════════════════════════════════\n" +
                        "           🍕 POS PIZZA           \n" +
                        "     Địa chỉ: 123 Đường ABC, TP.HCM     \n" +
                        "        ☎ Hotline: 1900-1234        \n" +
                        "════════════════════════════════");
        storeInfo.setFont(Font.font("Monospaced", 12));
        storeInfo.setAlignment(Pos.CENTER);
        storeInfo.setStyle("-fx-text-fill: #2c3e50;");

        // Order info  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Label orderInfo = new Label(
                "Đơn #: " + String.format("%04d", orderNum) + "\n" +
                        "Thời gian: " + LocalDateTime.now().format(formatter) + "\n" +
                        "Thu ngân: " + cashierLabel.getText() + "\n" +
                        "Khách hàng: " + customerName);
        orderInfo.setFont(Font.font("Monospaced", 11));
        orderInfo.setStyle("-fx-text-fill: #34495e;");

        // Items list
        Label itemsHeader = new Label("────────────────────────────────\n" +
                "         CHI TIẾT ĐƠN HÀNG         \n" +
                "────────────────────────────────");
        itemsHeader.setFont(Font.font("Monospaced", 11));
        itemsHeader.setAlignment(Pos.CENTER);

        VBox itemsList = new VBox(5);
        for (int i = 0; i < items.size(); i++) {
            Pizza pizza = items.get(i);
            Pizza basePizza = getBasePizza(pizza);

            String itemText = String.format("%d. %-25s %,9dđ",
                    i + 1,
                    basePizza.getName() + " (" + getSizeVietnamese(pizza.getSize()) + ")",
                    pizza.getPrice());

            Label itemLabel = new Label(itemText);
            itemLabel.setFont(Font.font("Monospaced", 11));
            itemsList.getChildren().add(itemLabel);

            // Show toppings if any
            if (!pizza.getDescription().equals(basePizza.getDescription())) {
                String toppings = pizza.getDescription()
                        .replace(basePizza.getDescription(), "")
                        .replace(", ", "\n   + ")
                        .trim();
                if (toppings.startsWith(",")) {
                    toppings = toppings.substring(1).trim();
                }
                Label toppingLabel = new Label("   + " + toppings);
                toppingLabel.setFont(Font.font("Monospaced", 10));
                toppingLabel.setStyle("-fx-text-fill: #7f8c8d;");
                itemsList.getChildren().add(toppingLabel);
            }
        }

        // Total section
        Label totalSection = new Label(
                "────────────────────────────────\n" +
                        String.format("%-25s %,9dđ", "TỔNG CỘNG:", total) + "\n" +
                        "────────────────────────────────\n" +
                        "Phương thức: " + getPaymentMethodVietnamese(paymentMethod) + "\n" +
                        "════════════════════════════════\n" +
                        "   🙏 CẢM ƠN QUÝ KHÁCH! 🙏   \n" +
                        "      HẸN GẶP LẠI!      \n" +
                        "════════════════════════════════");
        totalSection.setFont(Font.font("Monospaced", 11));
        totalSection.setAlignment(Pos.CENTER);
        totalSection.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        content.getChildren().addAll(
                storeInfo,
                new Separator(),
                orderInfo,
                itemsHeader,
                itemsList,
                totalSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(450, 500);

        receiptDialog.getDialogPane().setContent(scrollPane);
        receiptDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        receiptDialog.showAndWait();
    }

    @FXML
    private void handleCheckout() {
        if (cartService.getItemCount() == 0) {
            showAlert(Alert.AlertType.WARNING, "Giỏ hàng trống",
                    "Vui lòng thêm món trước khi thanh toán.");
            return;
        }

        try {
            // Process payment using Strategy Pattern
            boolean paymentSuccess = cartService.processPayment();

            if (!paymentSuccess) {
                showAlert(Alert.AlertType.ERROR, "Thanh toán thất bại",
                        "Không thể xử lý thanh toán. Vui lòng thử lại.");
                return;
            }

            // Save order to database
            String customerName = customerNameField.getText().trim();
            String paymentMethod = cartService.getPaymentStrategy().getName();

            int orderId = orderRepository.saveOrder(
                    customerName.isEmpty() ? null : customerName,
                    paymentMethod,
                    cartService.getItems(),
                    cartService.getTotal());

            // Save order data before clearing
            List<Pizza> orderItems = new ArrayList<>(cartService.getItems());
            int orderTotal = cartService.getTotal();
            String orderPaymentMethod = paymentMethod;
            String orderCustomerName = customerName.isEmpty() ? "Khách vãng lai" : customerName;
            int currentOrderNumber = orderCounter;

            // Show quick success notification
            showToast("✅ Thanh toán thành công! Đơn #" + orderId);

            // Clear cart and reset
            cartService.clear();
            customerNameField.clear();
            selectedItemIndex = -1;
            orderCounter++;
            orderNumberLabel.setText("Đơn #" + String.format("%04d", orderCounter));

            // Auto-print receipt after checkout
            showReceiptDialog(currentOrderNumber, orderCustomerName, orderPaymentMethod, orderItems, orderTotal);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu",
                    "Không thể lưu đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateOrderDisplay() {
        Platform.runLater(() -> {
            orderItemsBox.getChildren().clear();

            List<Pizza> items = cartService.getItems();
            for (int i = 0; i < items.size(); i++) {
                Pizza pizza = items.get(i);
                HBox itemBox = createOrderItemBox(i, pizza);
                orderItemsBox.getChildren().add(itemBox);
            }

            // Update totals
            int total = cartService.getTotal();
            totalLabel.setText(String.format("%,dđ", total));
        });
    }

    private HBox createOrderItemBox(int index, Pizza pizza) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(8, 10, 8, 10));
        box.getStyleClass().add("order-item-box");

        // Make clickable
        box.setOnMouseClicked(e -> {
            selectedItemIndex = index;
            // Highlight selected
            orderItemsBox.getChildren().forEach(node -> {
                node.getStyleClass().remove("selected");
            });
            box.getStyleClass().add("selected");
        });

        // Item number and description
        VBox infoBox = new VBox(4);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Get base pizza info (without toppings)
        Pizza basePizza = getBasePizza(pizza);
        String sizeName = getSizeVietnamese(basePizza.getSize());

        // Main label: Number + Pizza name + Size
        Label numberLabel = new Label((index + 1) + ". " + basePizza.getName() + " (" + sizeName + ")");
        numberLabel.setFont(Font.font("System Bold", 14));
        numberLabel.setWrapText(false);
        numberLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Topping label: Show toppings in second line if any
        Label toppingLabel = new Label();
        if (pizza instanceof ToppingDecorator) {
            String toppings = ((ToppingDecorator) pizza).getToppings();
            toppingLabel.setText("   + " + toppings);
            toppingLabel.setFont(Font.font("System", 12));
            toppingLabel.setStyle("-fx-text-fill: #95a5a6;");
            toppingLabel.setWrapText(true);
        }

        infoBox.getChildren().add(numberLabel);
        if (!toppingLabel.getText().isEmpty()) {
            infoBox.getChildren().add(toppingLabel);
        }

        // Price
        Label priceLabel = new Label(String.format("%,dđ", pizza.getPrice()));
        priceLabel.setFont(Font.font("System Bold", 16));
        priceLabel.setStyle("-fx-text-fill: #27ae60;");
        priceLabel.setMinWidth(85);
        priceLabel.setAlignment(Pos.CENTER_RIGHT);

        box.getChildren().addAll(infoBox, priceLabel);

        return box;
    }

    /**
     * Get Vietnamese size name.
     */
    private String getSizeVietnamese(Size size) {
        return switch (size) {
            case S -> "Nhỏ";
            case M -> "Vừa";
            case L -> "Lớn";
        };
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showToast(String message) {
        // Simple toast notification
        System.out.println("TOAST: " + message);
        // TODO: Implement proper toast notification UI
    }

    /**
     * Get Vietnamese payment method name.
     */
    private String getPaymentMethodVietnamese(String paymentMethod) {
        return switch (paymentMethod) {
            case "Cash" -> "Tiền mặt";
            case "Card" -> "Thẻ";
            case "E-Wallet" -> "Ví điện tử";
            default -> paymentMethod;
        };
    }
}
