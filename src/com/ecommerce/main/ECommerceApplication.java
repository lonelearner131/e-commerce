package com.ecommerce.main;

import com.ecommerce.discount.*;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.*;
import com.ecommerce.model.*;
import com.ecommerce.payment.*;
import com.ecommerce.service.*;
import com.ecommerce.util.InputHelper;

import java.util.List;

public class ECommerceApplication {

    private static final String[] DISCOUNT_CODES = {"SAVE10", "FLAT500"};

    private static UserService userService;
    private static ProductService productService;
    private static CartService cartService;
    private static PaymentService paymentService;
    private static OrderService orderService;

    public static void main(String[] args) {
        userService = new UserService();
        productService = new ProductService();
        cartService = new CartService(productService);
        paymentService = new PaymentService();
        orderService = new OrderService(productService, paymentService);

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     WELCOME TO THE E-COMMERCE MANAGEMENT SYSTEM     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> handleCustomerRegistration();
                case 2 -> handleCustomerLogin();
                case 3 -> handleAdminLogin();
                case 4 -> {
                    System.out.println("\n  Thank you for using the E-Commerce System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("  [!] Invalid choice. Please select 1-4.");
            }
        }
    }

    // ==================== MAIN MENU ====================

    private static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("  1. Customer Register");
        System.out.println("  2. Customer Login");
        System.out.println("  3. Admin Login");
        System.out.println("  4. Exit");
        System.out.println("===============================");
    }

    // ==================== CUSTOMER REGISTRATION ====================

    private static void handleCustomerRegistration() {
        System.out.println("\n--- CUSTOMER REGISTRATION ---");
        try {
            String name = InputHelper.readString("  Name     : ");
            String email = InputHelper.readString("  Email    : ");
            String password = InputHelper.readString("  Password : ");
            String address = InputHelper.readString("  Address  : ");

            Customer customer = userService.registerCustomer(name, email, password, address);
            System.out.println("\n  [✓] Registration successful! Welcome, " + customer.getName() + "!");
            customer.displayProfile();
        } catch (InvalidOperationException | IllegalArgumentException e) {
            System.out.println("  [!] Registration failed: " + e.getMessage());
        }
    }

    // ==================== CUSTOMER LOGIN ====================

    private static void handleCustomerLogin() {
        System.out.println("\n--- CUSTOMER LOGIN ---");
        String email = InputHelper.readString("  Email    : ");
        String password = InputHelper.readString("  Password : ");

        Customer customer = userService.loginCustomer(email, password);
        if (customer == null) {
            System.out.println("  [!] Invalid email or password.");
            return;
        }
        System.out.println("\n  [✓] Login successful! Welcome back, " + customer.getName() + "!");
        customerMenu(customer);
    }

    // ==================== ADMIN LOGIN ====================

    private static void handleAdminLogin() {
        System.out.println("\n--- ADMIN LOGIN ---");
        String email = InputHelper.readString("  Email    : ");
        String password = InputHelper.readString("  Password : ");

        Admin admin = userService.loginAdmin(email, password);
        if (admin == null) {
            System.out.println("  [!] Invalid admin credentials.");
            return;
        }
        System.out.println("\n  [✓] Admin login successful! Welcome, " + admin.getName() + "!");
        adminMenu(admin);
    }

    // ==================== CUSTOMER MENU ====================

    private static void customerMenu(Customer customer) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("  1.  View Products");
            System.out.println("  2.  Search Product");
            System.out.println("  3.  Add Product to Cart");
            System.out.println("  4.  View Cart");
            System.out.println("  5.  Remove Product from Cart");
            System.out.println("  6.  Update Cart Quantity");
            System.out.println("  7.  Apply Discount");
            System.out.println("  8.  Checkout & Make Payment");
            System.out.println("  9.  View My Orders");
            System.out.println("  10. Cancel an Order");
            System.out.println("  11. Logout");
            System.out.println("===================================");
            int choice = InputHelper.readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> viewProducts();
                    case 2 -> searchProduct();
                    case 3 -> addToCart(customer);
                    case 4 -> cartService.displayCart(customer);
                    case 5 -> removeFromCart(customer);
                    case 6 -> updateCartQuantity(customer);
                    case 7 -> applyDiscount(customer);
                    case 8 -> checkout(customer);
                    case 9 -> viewMyOrders(customer);
                    case 10 -> cancelOrder(customer);
                    case 11 -> {
                        System.out.println("  [✓] Logged out successfully.");
                        loggedIn = false;
                    }
                    default -> System.out.println("  [!] Invalid choice. Please select 1-11.");
                }
            } catch (ProductNotFoundException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (InsufficientStockException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (InvalidOperationException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("  [!] " + e.getMessage());
            }
        }
    }

    private static void viewProducts() {
        System.out.println("\n--- ALL PRODUCTS ---");
        productService.displayAllProducts();
    }

    private static void searchProduct() {
        String keyword = InputHelper.readString("  Enter search keyword: ");
        List<Product> results = productService.searchProductsByName(keyword);
        if (results.isEmpty()) {
            System.out.println("  No products found matching '" + keyword + "'.");
        } else {
            System.out.println("\n  Search Results (" + results.size() + " found):");
            System.out.println("  " + "-".repeat(95));
            System.out.printf("  | %-4s | %-22s | %-12s | %10s | %-6s | %s%n",
                    "ID", "Product Name", "Category", "Price", "Stock", "Details");
            System.out.println("  " + "-".repeat(95));
            for (Product p : results) {
                System.out.println("  " + p.toString());
            }
            System.out.println("  " + "-".repeat(95));
        }
    }

    private static void addToCart(Customer customer) {
        int productId = InputHelper.readInt("  Enter Product ID: ");
        int qty = InputHelper.readInt("  Enter Quantity  : ");
        cartService.addToCart(customer, productId, qty);
        System.out.println("  [✓] Product added to cart successfully!");
    }

    private static void removeFromCart(Customer customer) {
        int productId = InputHelper.readInt("  Enter Product ID to remove: ");
        cartService.removeFromCart(customer, productId);
        System.out.println("  [✓] Product removed from cart.");
    }

    private static void updateCartQuantity(Customer customer) {
        int productId = InputHelper.readInt("  Enter Product ID: ");
        int newQty = InputHelper.readInt("  Enter New Quantity (0 to remove): ");
        cartService.updateQuantity(customer, productId, newQty);
        System.out.println("  [✓] Cart updated successfully.");
    }

    private static void applyDiscount(Customer customer) {
        System.out.println("\n--- APPLY DISCOUNT ---");
        System.out.println("  Available Codes:");
        System.out.println("    1. SAVE10  - 10% off");
        System.out.println("    2. FLAT500 - \u20B9500 off on orders >= \u20B95000");
        System.out.println("    3. Remove Discount");
        int choice = InputHelper.readInt("  Select option: ");

        switch (choice) {
            case 1 -> {
                cartService.applyDiscount(customer, new PercentageDiscount("SAVE10", 10));
                System.out.println("  [✓] Discount code SAVE10 applied (10% off).");
            }
            case 2 -> {
                cartService.applyDiscount(customer, new FlatDiscount("FLAT500", 500, 5000));
                System.out.println("  [✓] Discount code FLAT500 applied (\u20B9500 off on orders >= \u20B95000).");
            }
            case 3 -> {
                cartService.applyDiscount(customer, new NoDiscount());
                System.out.println("  [✓] Discount removed.");
            }
            default -> System.out.println("  [!] Invalid option.");
        }
    }

    private static void checkout(Customer customer) {
        Cart cart = customer.getCart();
        if (cart.isEmpty()) {
            System.out.println("  [!] Your cart is empty. Add items before checkout.");
            return;
        }

        cartService.displayCart(customer);

        System.out.println("\n--- SELECT PAYMENT METHOD ---");
        System.out.println("  1. UPI");
        System.out.println("  2. Card");
        System.out.println("  3. Cash on Delivery");
        int payChoice = InputHelper.readInt("  Select payment method: ");

        Payment payment;
        switch (payChoice) {
            case 1 -> {
                String upiId = InputHelper.readString("  Enter UPI ID (e.g., user@upi): ");
                payment = new UpiPayment(upiId);
            }
            case 2 -> {
                String cardNumber = InputHelper.readString("  Enter Card Number (16 digits): ");
                String cardHolder = InputHelper.readString("  Enter Card Holder Name: ");
                String cvv = InputHelper.readString("  Enter CVV (3 digits): ");
                payment = new CardPayment(cardNumber, cardHolder, cvv);
            }
            case 3 -> payment = new CashPayment();
            default -> {
                System.out.println("  [!] Invalid payment method.");
                return;
            }
        }

        Order order = orderService.placeOrder(customer, payment);
        System.out.println("\n  [✓] Order placed successfully!");
        order.displayReceipt();
    }

    private static void viewMyOrders(Customer customer) {
        List<Order> myOrders = orderService.getOrdersByCustomer(customer);
        if (myOrders.isEmpty()) {
            System.out.println("  You have no orders yet.");
            return;
        }
        System.out.println("\n--- YOUR ORDERS ---");
        System.out.println("  " + "-".repeat(80));
        System.out.printf("  | %-8s | %-20s | %12s | %-12s | %-20s |%n",
                "Order ID", "Date", "Total", "Status", "Payment");
        System.out.println("  " + "-".repeat(80));
        for (Order o : myOrders) {
            System.out.printf("  | %-8d | %-20s | %12s | %-12s | %-20s |%n",
                    o.getOrderId(),
                    o.getOrderDate().toLocalDate().toString(),
                    "\u20B9" + String.format("%.2f", o.getFinalAmount()),
                    o.getStatus(),
                    o.getPaymentMethod());
        }
        System.out.println("  " + "-".repeat(80));

        String viewDetail = InputHelper.readString("  View full receipt? (y/n): ");
        if (viewDetail.equalsIgnoreCase("y")) {
            int orderId = InputHelper.readInt("  Enter Order ID: ");
            for (Order o : myOrders) {
                if (o.getOrderId() == orderId) {
                    o.displayReceipt();
                    return;
                }
            }
            System.out.println("  [!] Order not found in your orders.");
        }
    }

    private static void cancelOrder(Customer customer) {
        int orderId = InputHelper.readInt("  Enter Order ID to cancel: ");
        orderService.cancelOrder(orderId, customer);
        System.out.println("  [✓] Order #" + orderId + " has been cancelled. Stock restored.");
    }

    // ==================== ADMIN MENU ====================

    private static void adminMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("  1. Add Product");
            System.out.println("  2. View All Products");
            System.out.println("  3. Search Product");
            System.out.println("  4. Update Product Details / Price");
            System.out.println("  5. Update Product Stock");
            System.out.println("  6. Remove Product");
            System.out.println("  7. View All Customer Orders");
            System.out.println("  8. Update Order Status");
            System.out.println("  9. Logout");
            System.out.println("================================");
            int choice = InputHelper.readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> adminAddProduct();
                    case 2 -> viewProducts();
                    case 3 -> searchProduct();
                    case 4 -> adminUpdateProduct();
                    case 5 -> adminUpdateStock();
                    case 6 -> adminRemoveProduct();
                    case 7 -> adminViewAllOrders();
                    case 8 -> adminUpdateOrderStatus();
                    case 9 -> {
                        System.out.println("  [✓] Admin logged out successfully.");
                        loggedIn = false;
                    }
                    default -> System.out.println("  [!] Invalid choice. Please select 1-9.");
                }
            } catch (ProductNotFoundException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (InsufficientStockException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (InvalidOperationException e) {
                System.out.println("  [!] " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("  [!] " + e.getMessage());
            }
        }
    }

    private static void adminAddProduct() {
        System.out.println("\n--- ADD NEW PRODUCT ---");
        System.out.println("  Select Category:");
        for (int i = 0; i < ProductService.CATEGORIES.length; i++) {
            System.out.println("    " + (i + 1) + ". " + ProductService.CATEGORIES[i]);
        }
        int catChoice = InputHelper.readInt("  Category: ");
        if (catChoice < 1 || catChoice > ProductService.CATEGORIES.length) {
            System.out.println("  [!] Invalid category.");
            return;
        }

        int id = InputHelper.readInt("  Product ID  : ");
        String name = InputHelper.readString("  Product Name: ");
        double price = InputHelper.readDouble("  Price (\u20B9)  : ");
        int stock = InputHelper.readInt("  Stock Qty   : ");

        Product product;
        switch (catChoice) {
            case 1 -> {
                int warranty = InputHelper.readInt("  Warranty (months): ");
                product = new Electronics(id, name, price, stock, warranty);
            }
            case 2 -> {
                String size = InputHelper.readString("  Size  : ");
                String brand = InputHelper.readString("  Brand : ");
                product = new Clothing(id, name, price, stock, size, brand);
            }
            case 3 -> {
                String expiry = InputHelper.readString("  Expiry Date (YYYY-MM-DD): ");
                product = new Grocery(id, name, price, stock, expiry);
            }
            default -> {
                System.out.println("  [!] Invalid category.");
                return;
            }
        }

        productService.addProduct(product);
        System.out.println("  [✓] Product added successfully: " + product.getName() + " (ID: " + product.getProductId() + ")");
    }

    private static void adminUpdateProduct() {
        int id = InputHelper.readInt("  Enter Product ID to update: ");
        Product product = productService.getProductById(id);
        System.out.println("  Current: " + product.toString());

        System.out.println("  What do you want to update?");
        System.out.println("    1. Name");
        System.out.println("    2. Price");
        System.out.println("    3. Both");
        int option = InputHelper.readInt("  Select option: ");

        switch (option) {
            case 1 -> {
                String newName = InputHelper.readString("  New Name: ");
                productService.updateProductDetails(id, newName);
                System.out.println("  [✓] Product name updated.");
            }
            case 2 -> {
                double newPrice = InputHelper.readDouble("  New Price (\u20B9): ");
                productService.updatePrice(id, newPrice);
                System.out.println("  [✓] Product price updated.");
            }
            case 3 -> {
                String newName = InputHelper.readString("  New Name: ");
                double newPrice = InputHelper.readDouble("  New Price (\u20B9): ");
                productService.updateProductDetails(id, newName);
                productService.updatePrice(id, newPrice);
                System.out.println("  [✓] Product name and price updated.");
            }
            default -> System.out.println("  [!] Invalid option.");
        }
    }

    private static void adminUpdateStock() {
        int id = InputHelper.readInt("  Enter Product ID: ");
        Product product = productService.getProductById(id);
        System.out.println("  Current stock for '" + product.getName() + "': " + product.getStockQuantity());
        int newStock = InputHelper.readInt("  New Stock Quantity: ");
        productService.updateStock(id, newStock);
        System.out.println("  [✓] Stock updated to " + newStock + ".");
    }

    private static void adminRemoveProduct() {
        int id = InputHelper.readInt("  Enter Product ID to remove: ");
        Product product = productService.getProductById(id);
        String confirm = InputHelper.readString("  Are you sure you want to remove '" + product.getName() + "'? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            productService.removeProduct(id);
            System.out.println("  [✓] Product removed successfully.");
        } else {
            System.out.println("  [i] Removal cancelled.");
        }
    }

    private static void adminViewAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        if (allOrders.isEmpty()) {
            System.out.println("  No orders have been placed yet.");
            return;
        }
        System.out.println("\n--- ALL CUSTOMER ORDERS ---");
        System.out.println("  " + "-".repeat(96));
        System.out.printf("  | %-8s | %-15s | %-20s | %12s | %-12s | %-16s |%n",
                "Order ID", "Customer", "Email", "Total", "Status", "Payment");
        System.out.println("  " + "-".repeat(96));
        for (Order o : allOrders) {
            System.out.printf("  | %-8d | %-15s | %-20s | %12s | %-12s | %-16s |%n",
                    o.getOrderId(),
                    o.getCustomer().getName(),
                    o.getCustomer().getEmail(),
                    "\u20B9" + String.format("%.2f", o.getFinalAmount()),
                    o.getStatus(),
                    o.getPaymentMethod());
        }
        System.out.println("  " + "-".repeat(96));
    }

    private static void adminUpdateOrderStatus() {
        int orderId = InputHelper.readInt("  Enter Order ID: ");
        System.out.println("  Select new status:");
        System.out.println("    1. SHIPPED");
        System.out.println("    2. DELIVERED");
        System.out.println("    3. CANCELLED");
        int statusChoice = InputHelper.readInt("  Select option: ");

        OrderStatus newStatus;
        switch (statusChoice) {
            case 1 -> newStatus = OrderStatus.SHIPPED;
            case 2 -> newStatus = OrderStatus.DELIVERED;
            case 3 -> newStatus = OrderStatus.CANCELLED;
            default -> {
                System.out.println("  [!] Invalid status option.");
                return;
            }
        }

        orderService.updateOrderStatus(orderId, newStatus);
        System.out.println("  [✓] Order #" + orderId + " status updated to " + newStatus + ".");
    }
}
