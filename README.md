# 🍕 Ứng Dụng Đặt Pizza

Ứng dụng JavaFX desktop toàn diện minh họa các nguyên lý Lập trình Hướng đối tượng (OOP) và bốn mẫu thiết kế thiết yếu: **Factory**, **Decorator**, **Strategy**, và **Observer**.

## 📋 Mục Lục
- [Tính Năng](#tính-năng)
- [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
- [Các Mẫu Thiết Kế](#các-mẫu-thiết-kế)
- [Cấu Trúc Dự Án](#cấu-trúc-dự-án)
- [Bắt Đầu](#bắt-đầu)
- [Hướng Dẫn Sử Dụng](#hướng-dẫn-sử-dụng)
- [Kiểm Thử](#kiểm-thử)
- [Cơ Sở Dữ Liệu](#cơ-sở-dữ-liệu)
- [Nguyên Lý OOP](#nguyên-lý-oop)

## ✨ Tính Năng

- **Tùy Chỉnh Pizza**: Chọn từ nhiều loại pizza (Margherita, Pepperoni, Hawaiian, Seafood, Veggie Supreme) và kích thước (S, M, L)
- **Thêm Topping Linh Hoạt**: Thêm topping bổ sung (phô mai, thịt xông khói, nấm) với cập nhật giá theo thời gian thực
- **Giỏ Hàng**: Xem và quản lý đơn hàng với tính toán tổng tiền tự động
- **Phương Thức Vận Chuyển Linh Hoạt**: Chọn giữa vận chuyển Tiêu chuẩn và Hỏa tốc với các chiến lược định giá khác nhau
- **Lưu Trữ Đơn Hàng**: Tất cả đơn hàng được lưu vào cơ sở dữ liệu SQLite
- **Giao Diện Hiện Đại**: Giao diện JavaFX trực quan, sạch sẽ với tùy chỉnh CSS

## 🛠 Công Nghệ Sử Dụng

- **Java**: 17
- **JavaFX**: 19.0.2.1
- **SQLite**: 3.41.2.2 (qua JDBC)
- **JUnit**: 5.9.2
- **Công Cụ Build**: Maven

## 🎯 Các Mẫu Thiết Kế

### 1. Factory Pattern (Mẫu Nhà Máy) 🏭
**Vị trí**: `com.pizza.domain.factory.PizzaFactory`

**Mục đích**: Tạo các đối tượng pizza mà không để lộ logic khởi tạo cho client.

**Cách triển khai**:
```java
Pizza pizza = PizzaFactory.create("Margherita", Size.M);
```

**Lợi ích**:
- Đóng gói logic tạo đối tượng
- Dễ dàng thêm loại pizza mới
- Tuân theo nguyên lý Open/Closed (Mở để mở rộng, đóng để sửa đổi)

**Tham chiếu code**: Xem `PizzaFactory.java` dòng 30-43

---

### 2. Decorator Pattern (Mẫu Trang Trí) 🎨
**Vị trí**: `com.pizza.domain.decorator.*`

**Mục đích**: Thêm topping động vào pizza mà không sửa đổi các class Pizza cơ bản.

**Cách triển khai**:
```java
Pizza pizza = PizzaFactory.create("Pepperoni", Size.L);
pizza = new Cheese(pizza);      // Thêm topping phô mai
pizza = new Bacon(pizza);       // Thêm topping thịt xông khói
pizza = new Mushroom(pizza);    // Thêm topping nấm
```

**Lợi ích**:
- Mở rộng chức năng tại runtime
- Tuân theo nguyên lý Single Responsibility (Trách nhiệm đơn lẻ)
- Thay thế linh hoạt cho việc phân lớp con
- Mỗi topping có thể được thêm độc lập

**Tham chiếu code**: 
- Abstract Decorator: `ToppingDecorator.java`
- Concrete Decorators: `Cheese.java`, `Bacon.java`, `Mushroom.java`

---

### 3. Strategy Pattern (Mẫu Chiến Lược) 📦
**Vị trí**: `com.pizza.domain.strategy.*`

**Mục đích**: Định nghĩa các thuật toán tính phí vận chuyển có thể thay thế lẫn nhau.

**Cách triển khai**:
```java
ShippingStrategy standard = new StandardShipping();
int fee = standard.calculateFee(subtotal);

ShippingStrategy express = new ExpressShipping();
int fee = express.calculateFee(subtotal);
```

**Các chiến lược**:
- **StandardShipping**: Miễn phí nếu tổng tiền ≥ 200,000 VND, ngược lại 20,000 VND
- **ExpressShipping**: Phí cố định 40,000 VND

**Lợi ích**:
- Đóng gói các thuật toán trong các class riêng biệt
- Làm cho các thuật toán có thể thay thế lẫn nhau
- Dễ dàng thêm phương thức vận chuyển mới
- Loại bỏ các câu lệnh điều kiện

**Tham chiếu code**: 
- Interface: `ShippingStrategy.java`
- Implementations: `StandardShipping.java`, `ExpressShipping.java`

---

### 4. Observer Pattern (Mẫu Quan Sát) 👁️
**Vị trí**: `com.pizza.app.EventBus`

**Mục đích**: Thông báo cho các component UI khi giỏ hàng thay đổi, cho phép cập nhật tự động.

**Cách triển khai**:
```java
// Đăng ký nhận cập nhật giỏ hàng
EventBus.getInstance().subscribe("CART_UPDATED", data -> {
    updateCartDisplay();
});

// Phát sự kiện khi giỏ hàng thay đổi
EventBus.getInstance().publish("CART_UPDATED");
```

**Lợi ích**:
- Tách rời người phát sự kiện khỏi người đăng ký
- Cập nhật UI tự động khi dữ liệu thay đổi
- Hỗ trợ nhiều observer
- Thúc đẩy loose coupling (liên kết lỏng lẻo)

**Tham chiếu code**: 
- EventBus: `EventBus.java`
- Publisher: `CartService.java` (dòng 43, 53, 60, 73)
- Subscribers: `MenuController.java` (dòng 45), `CartController.java` (dòng 73)

---

## 📁 Cấu Trúc Dự Án

```
pizza-order-app/
├── src/
│   ├── main/
│   │   ├── java/com/pizza/
│   │   │   ├── domain/
│   │   │   │   ├── pizza/          # Phân cấp Pizza (OOP)
│   │   │   │   │   ├── Pizza.java           (Base trừu tượng)
│   │   │   │   │   ├── Size.java            (Enum)
│   │   │   │   │   ├── Margherita.java      (Concrete)
│   │   │   │   │   ├── Pepperoni.java       (Concrete)
│   │   │   │   │   ├── Hawaiian.java        (Concrete)
│   │   │   │   │   ├── Seafood.java         (Concrete)
│   │   │   │   │   └── VeggieSupreme.java   (Concrete)
│   │   │   │   ├── decorator/      # Decorator Pattern
│   │   │   │   │   ├── ToppingDecorator.java (Trừu tượng)
│   │   │   │   │   ├── Cheese.java          (Concrete)
│   │   │   │   │   ├── Bacon.java           (Concrete)
│   │   │   │   │   └── Mushroom.java        (Concrete)
│   │   │   │   ├── strategy/       # Strategy Pattern
│   │   │   │   │   ├── ShippingStrategy.java (Interface)
│   │   │   │   │   ├── StandardShipping.java
│   │   │   │   │   └── ExpressShipping.java
│   │   │   │   └── factory/        # Factory Pattern
│   │   │   │       └── PizzaFactory.java
│   │   │   ├── app/                # Application Layer
│   │   │   │   ├── EventBus.java        (Observer Pattern)
│   │   │   │   ├── CartService.java     (Business Logic)
│   │   │   │   └── PriceCalculator.java
│   │   │   ├── infra/db/           # Database Layer
│   │   │   │   ├── SQLiteConnection.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── PizzaRepository.java
│   │   │   └── ui/                 # UI Layer
│   │   │       ├── MainApp.java
│   │   │       ├── Launcher.java
│   │   │       └── controllers/
│   │   │           ├── HomeController.java
│   │   │           ├── MenuController.java
│   │   │           └── CartController.java
│   │   └── resources/
│   │       ├── views/
│   │       │   ├── home.fxml
│   │       │   ├── menu.fxml
│   │       │   └── cart.fxml
│   │       └── application.css
│   └── test/
│       └── java/com/pizza/
│           ├── PriceCalculatorTest.java
│           ├── StrategyTest.java
│           └── DecoratorTest.java
├── pom.xml
└── README.md
```

## 🚀 Bắt Đầu

### Yêu Cầu

- **Java JDK 17 trở lên**
- **Maven 3.6+**

### Cài Đặt

1. **Clone hoặc giải nén dự án**:
   ```bash
   cd pizza-order-app
   ```

2. **Kiểm tra phiên bản Maven và Java**:
   ```bash
   mvn -v
   java -version
   ```

3. **Build dự án**:
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng**:
   ```bash
   mvn javafx:run
   ```

## 📖 Hướng Dẫn Sử Dụng

### Quy Trình Chính

1. **Màn Hình Chính**: 
   - Click "Browse Menu" (Xem Menu) để bắt đầu đặt hàng
   - Click "View Cart / Checkout" (Xem Giỏ Hàng / Thanh Toán) để xem giỏ hàng

2. **Màn Hình Menu**:
   - Chọn loại pizza (Margherita, Pepperoni, Hawaiian, Seafood, hoặc Veggie Supreme)
   - Chọn kích thước (S, M, L)
   - Thêm topping bổ sung (tùy chọn)
   - Đặt số lượng
   - Click "Add to Cart" (Thêm vào Giỏ)
   - Xem giá cả hiển thị theo thời gian thực

3. **Màn Hình Giỏ Hàng**:
   - Xem lại các món đã chọn
   - Xóa món nếu cần
   - Chọn phương thức vận chuyển:
     - **Tiêu chuẩn (Standard)**: Miễn phí cho đơn hàng ≥200k, ngược lại 20k
     - **Hỏa tốc (Express)**: Phí cố định 40k
   - Xem tổng tiền tự động tính toán
   - Nhập thông tin giao hàng
   - Click "Place Order" (Đặt Hàng) để hoàn tất

### Bảng Giá

**Giá Cơ Bản Theo Loại Pizza**:
- Margherita: 60,000 VND
- Pepperoni: 90,000 VND
- Hawaiian: 80,000 VND
- Seafood: 120,000 VND
- Veggie Supreme: 70,000 VND

**Điều Chỉnh Theo Kích Thước**:
- Small (S): +0 VND
- Medium (M): +15,000 VND
- Large (L): +30,000 VND

**Giá Topping**:
- Extra Cheese (Phô mai): 10,000 VND
- Bacon (Thịt xông khói): 15,000 VND
- Mushroom (Nấm): 8,000 VND

**Ví dụ**:
- Pepperoni Large (120,000) + Cheese (10,000) + Bacon (15,000) = **145,000 VND**
- Seafood Medium (135,000) + Mushroom (8,000) = **143,000 VND**
- Veggie Supreme Small (70,000) + Cheese (10,000) = **80,000 VND**

## 🧪 Kiểm Thử

### Chạy Tất Cả Tests

```bash
mvn test
```

### Phạm Vi Test

Dự án bao gồm các unit test toàn diện:

1. **PriceCalculatorTest**: Kiểm tra tính toán giá với decorator và strategy
2. **StrategyTest**: Kiểm tra tính toán phí vận chuyển và các điều kiện biên
3. **DecoratorTest**: Kiểm tra decoration topping và tích lũy giá

### Ví Dụ Kết Quả Test

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.pizza.DecoratorTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.pizza.PriceCalculatorTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.pizza.StrategyTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
```

## 💾 Cơ Sở Dữ Liệu

Ứng dụng sử dụng SQLite để lưu trữ đơn hàng. File cơ sở dữ liệu `pizza_orders.db` được tạo tự động trong thư mục gốc dự án khi chạy lần đầu.

**Schema**:

```sql
-- Bảng đơn hàng
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    address TEXT NOT NULL,
    shipping_type TEXT NOT NULL,
    subtotal INTEGER NOT NULL,
    shipping_fee INTEGER NOT NULL,
    total INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng chi tiết đơn hàng
CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    pizza_name TEXT NOT NULL,
    size TEXT NOT NULL,
    toppings TEXT,
    price INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
```

## 🎓 Nguyên Lý OOP

Dự án minh họa các nguyên lý lập trình hướng đối tượng:

1. **Encapsulation (Đóng gói)**: Các trường private với getter/setter public (Pizza, CartService)
2. **Inheritance (Kế thừa)**: Phân cấp Pizza (Margherita, Pepperoni, Hawaiian, Seafood, VeggieSupreme kế thừa Pizza)
3. **Polymorphism (Đa hình)**: Interface ShippingStrategy với nhiều implementation
4. **Abstraction (Trừu tượng hóa)**: Abstract classes (Pizza, ToppingDecorator) và interfaces (ShippingStrategy)

## 📝 Giấy Phép

Đây là dự án giáo dục để minh họa các mẫu thiết kế và nguyên lý OOP.

## 👨‍💻 Tác Giả

**Đỗ Trung Tú - 2212485**

Dự án được tạo ra như một minh chứng toàn diện về các mẫu thiết kế trong Java.

---

**Chúc bạn đặt pizza vui vẻ! 🍕**
