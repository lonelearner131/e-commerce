# 🛒 E-Commerce Management System

A fully functional, console-based E-Commerce Management System built with **Core Java (JDK 17+)** — no external libraries, no database, no frameworks. Pure object-oriented programming with in-memory data storage.

---

## ✨ Features

### Customer Portal
- Browse and search the product catalog
- Add/remove/update items in a shopping cart
- Apply discount codes (`SAVE10` for 10% off, `FLAT500` for ₹500 off on orders ≥ ₹5000)
- Checkout with multiple payment methods (UPI, Card, Cash on Delivery)
- View order history with itemized receipts
- Cancel orders (with automatic stock restoration)

### Admin Dashboard
- Add new products (Electronics, Clothing, Grocery) with category-specific attributes
- Update product details, pricing, and stock levels
- Remove products from the catalog
- View all customer orders across the platform
- Manage order lifecycle (Ship → Deliver → Cancel)

---

## 🏗️ Architecture

```
src/
└── com/
    └── ecommerce/
        ├── enums/           # Role, OrderStatus
        ├── exception/       # Custom runtime exceptions
        ├── model/           # Domain entities (User, Product, Cart, Order hierarchies)
        ├── payment/         # Payment interface & implementations (UPI, Card, COD)
        ├── discount/        # Discount interface & implementations (Percentage, Flat, None)
        ├── service/         # Business logic layer
        ├── util/            # InputHelper (safe Scanner wrapper)
        └── main/            # Application entry point
```

### Class Diagram

```
User (abstract)                    Product (abstract)
├── Customer                       ├── Electronics
└── Admin                          ├── Clothing
                                   └── Grocery

«interface» Payment                «interface» Discount
├── UpiPayment                     ├── NoDiscount
├── CardPayment                    ├── PercentageDiscount
└── CashPayment                   └── FlatDiscount
```

### OOP Concepts Demonstrated

| Concept | Implementation |
|---|---|
| **Abstraction** | `User` and `Product` abstract classes with abstract methods |
| **Inheritance** | `Customer`/`Admin` extend `User`; `Electronics`/`Clothing`/`Grocery` extend `Product` |
| **Polymorphism** | `Payment` and `Discount` interfaces with multiple implementations |
| **Encapsulation** | All fields `private` with validation in constructors and setters |
| **Static Members** | Auto-incrementing ID counters for Users and Orders |
| **Collections** | `HashMap` for O(1) product lookup, `ArrayList` for orders, `HashSet` for unique emails |
| **Custom Exceptions** | `ProductNotFoundException`, `InsufficientStockException`, `InvalidOperationException` |

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17** or higher

### Build & Run

```bash
# Clone the repository
git clone https://github.com/<your-username>/e-commerce.git
cd e-commerce

# Compile
mkdir -p out
javac -d out $(find src -name "*.java")

# Run
java -cp out com.ecommerce.main.ECommerceApplication
```

**Windows (PowerShell):**

```powershell
mkdir -Force out
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out com.ecommerce.main.ECommerceApplication
```

### Demo Credentials

| Role | Email | Password |
|---|---|---|
| Admin | `admin@shop.com` | `admin123` |
| Customer | `john@gmail.com` | `john123` |

---

## 📋 Pre-loaded Catalog

| ID | Product | Category | Price (₹) | Stock | Details |
|----|---------|----------|-----------|-------|---------|
| 101 | Laptop | Electronics | 55,000 | 10 | 12-month warranty |
| 102 | Smartphone | Electronics | 25,000 | 15 | 12-month warranty |
| 201 | T-Shirt | Clothing | 999 | 25 | Size L, UrbanWear |
| 202 | Jeans | Clothing | 1,999 | 20 | Size 32, DenimCo |
| 301 | Basmati Rice (5kg) | Grocery | 650 | 40 | Exp: 2027-12-31 |
| 302 | Cooking Oil (1L) | Grocery | 180 | 50 | Exp: 2027-06-30 |

---

## 🔄 Order Lifecycle

```
CREATED ──► CONFIRMED ──► SHIPPED ──► DELIVERED
                │             │
                └─── CANCELLED ◄──┘
```

- Orders move forward: Confirmed → Shipped → Delivered
- Cancellation is allowed before delivery (stock is automatically restored)
- Delivered and already-cancelled orders cannot be cancelled again

---

## 🛡️ Business Rules

1. **Unique Product IDs** — No two products can share the same ID
2. **Unique Emails** — Customer registration enforces unique email addresses
3. **Stock Validation** — Cart quantities cannot exceed available stock at any point
4. **Empty Cart Guard** — Checkout is blocked if the cart is empty
5. **Payment-first Confirmation** — Stock is only deducted after successful payment
6. **Input Safety** — All console input uses `nextLine()` parsing (no `nextInt()`/`nextDouble()` scanner bugs)
7. **Graceful Error Handling** — Custom exceptions are caught and displayed as user-friendly messages without crashing

---

## 📂 File Overview

| File | Description |
|---|---|
| `enums/Role.java` | `CUSTOMER`, `ADMIN` |
| `enums/OrderStatus.java` | `CREATED`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED` |
| `exception/*.java` | Three custom `RuntimeException` subclasses |
| `model/User.java` | Abstract base with static ID counter, profile display |
| `model/Customer.java` | Extends User — has address and shopping cart |
| `model/Admin.java` | Extends User — admin role |
| `model/Product.java` | Abstract base with price/stock validation |
| `model/Electronics.java` | Product with warranty period |
| `model/Clothing.java` | Product with size and brand |
| `model/Grocery.java` | Product with expiry date |
| `model/CartItem.java` | Product + quantity wrapper with copy constructor |
| `model/Cart.java` | Item list with discount application and totals |
| `model/Order.java` | Immutable snapshot of cart at checkout time |
| `payment/Payment.java` | Interface: `processPayment()`, `getPaymentMethodName()` |
| `payment/UpiPayment.java` | Validates UPI ID format (must contain `@`) |
| `payment/CardPayment.java` | Validates 16-digit card number and 3-digit CVV |
| `payment/CashPayment.java` | Cash on Delivery — always succeeds |
| `discount/Discount.java` | Interface: `calculateDiscount()`, `getDescription()` |
| `discount/NoDiscount.java` | Returns 0 (default) |
| `discount/PercentageDiscount.java` | Percentage-based (e.g., 10% off) |
| `discount/FlatDiscount.java` | Flat amount off with minimum order threshold |
| `service/UserService.java` | Registration, login, unique email enforcement |
| `service/ProductService.java` | CRUD operations on the product catalog |
| `service/CartService.java` | Cart manipulation with stock validation |
| `service/PaymentService.java` | Payment execution delegate |
| `service/OrderService.java` | Order placement, cancellation, status transitions |
| `util/InputHelper.java` | Safe `Scanner` wrapper with retry loops |
| `main/ECommerceApplication.java` | Console UI with main, customer, and admin menus |

---

## 🧰 Tech Stack

- **Language:** Java 17+
- **Build:** `javac` (no Maven/Gradle required)
- **External Dependencies:** None
- **Database:** None (in-memory `HashMap`, `ArrayList`, `HashSet`)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
