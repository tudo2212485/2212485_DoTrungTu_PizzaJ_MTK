# 🍕 Pizza Order Application

A comprehensive JavaFX desktop application demonstrating Object-Oriented Programming principles and four essential design patterns: **Factory**, **Decorator**, **Strategy**, and **Observer**.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing](#testing)

## ✨ Features

- **Pizza Customization**: Choose from multiple pizza types (Margherita, Pepperoni, Hawaiian) and sizes (S, M, L)
- **Dynamic Toppings**: Add extra toppings (cheese, bacon, mushroom) with real-time price updates
- **Shopping Cart**: View and manage your order with automatic total calculations
- **Flexible Shipping**: Select between Standard and Express shipping with different pricing strategies
- **Order Persistence**: All orders are saved to a SQLite database
- **Modern UI**: Clean, intuitive JavaFX interface with custom styling

## 🛠 Technology Stack

- **Java**: 17
- **JavaFX**: 19.0.2.1
- **SQLite**: 3.41.2.2 (via JDBC)
- **JUnit**: 5.9.2
- **Build Tool**: Maven

## 🎯 Design Patterns

### 1. Factory Pattern 🏭
**Location**: `com.pizza.domain.factory.PizzaFactory`

**Purpose**: Creates pizza objects without exposing instantiation logic to the client.

**Implementation**:
```java
Pizza pizza = PizzaFactory.create("Margherita", Size.M);
```

**Benefits**:
- Encapsulates object creation logic
- Makes it easy to add new pizza types
- Follows the Open/Closed Principle

**Code Reference**: See `PizzaFactory.java` lines 14-30

---

### 2. Decorator Pattern 🎨
**Location**: `com.pizza.domain.decorator.*`

**Purpose**: Dynamically adds toppings to pizzas without modifying the base Pizza classes.

**Implementation**:
```java
Pizza pizza = PizzaFactory.create("Pepperoni", Size.L);
pizza = new Cheese(pizza);      // Add cheese topping
pizza = new Bacon(pizza);       // Add bacon topping
pizza = new Mushroom(pizza);    // Add mushroom topping
```

**Benefits**:
- Extends functionality at runtime
- Follows Single Responsibility Principle
- Flexible alternative to subclassing
- Each topping can be added independently

**Code Reference**: 
- Abstract Decorator: `ToppingDecorator.java`
- Concrete Decorators: `Cheese.java`, `Bacon.java`, `Mushroom.java`

---

### 3. Strategy Pattern 📦
**Location**: `com.pizza.domain.strategy.*`

**Purpose**: Defines interchangeable shipping fee calculation algorithms.

**Implementation**:
```java
ShippingStrategy standard = new StandardShipping();
int fee = standard.calculateFee(subtotal);

ShippingStrategy express = new ExpressShipping();
int fee = express.calculateFee(subtotal);
```

**Strategies**:
- **StandardShipping**: Free if subtotal ≥ 200,000 VND, otherwise 20,000 VND
- **ExpressShipping**: Flat fee of 40,000 VND

**Benefits**:
- Encapsulates algorithms in separate classes
- Makes algorithms interchangeable
- Easy to add new shipping methods
- Eliminates conditional statements

**Code Reference**: 
- Interface: `ShippingStrategy.java`
- Implementations: `StandardShipping.java`, `ExpressShipping.java`

---

### 4. Observer Pattern 👁️
**Location**: `com.pizza.app.EventBus`

**Purpose**: Notifies UI components when the cart changes, enabling automatic updates.

**Implementation**:
```java
// Subscribe to cart updates
EventBus.getInstance().subscribe("CART_UPDATED", data -> {
    updateCartDisplay();
});

// Publish event when cart changes
EventBus.getInstance().publish("CART_UPDATED");
```

**Benefits**:
- Decouples event publishers from subscribers
- Automatic UI updates when data changes
- Supports multiple observers
- Promotes loose coupling

**Code Reference**: 
- EventBus: `EventBus.java`
- Publisher: `CartService.java` (lines 43, 53, 60, 73)
- Subscribers: `MenuController.java` (line 45), `CartController.java` (line 73)

---

## 📁 Project Structure

```
pizza-order-app/
├── src/
│   ├── main/
│   │   ├── java/com/pizza/
│   │   │   ├── domain/
│   │   │   │   ├── pizza/          # Pizza hierarchy (OOP)
│   │   │   │   │   ├── Pizza.java           (Abstract base)
│   │   │   │   │   ├── Size.java            (Enum)
│   │   │   │   │   ├── Margherita.java      (Concrete)
│   │   │   │   │   ├── Pepperoni.java       (Concrete)
│   │   │   │   │   └── Hawaiian.java        (Concrete)
│   │   │   │   ├── decorator/      # Decorator Pattern
│   │   │   │   │   ├── ToppingDecorator.java (Abstract)
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
│   │   │   │   └── OrderRepository.java
│   │   │   └── ui/                 # UI Layer
│   │   │       ├── MainApp.java
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

## 🚀 Getting Started

### Prerequisites

- **Java JDK 17 or higher**
- **Maven 3.6+**

### Installation

1. **Clone or extract the project**:
   ```bash
   cd pizza-order-app
   ```

2. **Verify Maven and Java versions**:
   ```bash
   mvn -v
   java -version
   ```

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn javafx:run
   ```

## 📖 Usage

### Main Workflow

1. **Home Screen**: 
   - Click "Browse Menu" to start ordering
   - Click "View Cart / Checkout" to see your cart

2. **Menu Screen**:
   - Select pizza type (Margherita, Pepperoni, or Hawaiian)
   - Choose size (S, M, L)
   - Add extra toppings (optional)
   - Set quantity
   - Click "Add to Cart"
   - View real-time price preview

3. **Cart Screen**:
   - Review your items
   - Remove items if needed
   - Select shipping method:
     - **Standard**: Free for orders ≥200k, otherwise 20k
     - **Express**: Flat 40k
   - View automatic total calculation
   - Enter delivery information
   - Click "Place Order" to complete

### Pricing

**Base Prices by Pizza Type**:
- Margherita: 60,000 VND
- Pepperoni: 90,000 VND
- Hawaiian: 80,000 VND

**Size Modifiers**:
- Small (S): +0 VND
- Medium (M): +15,000 VND
- Large (L): +30,000 VND

**Topping Prices**:
- Extra Cheese: 10,000 VND
- Bacon: 15,000 VND
- Mushroom: 8,000 VND

**Example**:
- Pepperoni Large (120,000) + Cheese (10,000) + Bacon (15,000) = **145,000 VND**

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Test Coverage

The project includes comprehensive unit tests:

1. **PriceCalculatorTest**: Tests price calculations with decorators and strategies
2. **StrategyTest**: Tests shipping fee calculations and boundary conditions
3. **DecoratorTest**: Tests topping decoration and price accumulation

### Example Test Run Output

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

## 💾 Database

The application uses SQLite to persist orders. The database file `pizza_orders.db` is created automatically in the project root on first run.

**Schema**:

```sql
-- Orders table
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

-- Order items table
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

## 🎓 OOP Principles Demonstrated

1. **Encapsulation**: Private fields with public getters (Pizza, CartService)
2. **Inheritance**: Pizza hierarchy (Margherita, Pepperoni, Hawaiian extend Pizza)
3. **Polymorphism**: ShippingStrategy interface with multiple implementations
4. **Abstraction**: Abstract classes (Pizza, ToppingDecorator) and interfaces (ShippingStrategy)

## 📝 License

This is an educational project for demonstrating design patterns and OOP principles.

## 👨‍💻 Author

Created as a comprehensive demonstration of design patterns in Java.

---

**Enjoy ordering pizza! 🍕**






