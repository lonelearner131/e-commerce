package com.ecommerce.model;

import com.ecommerce.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private static int nextOrderId = 1001;

    private int orderId;
    private Customer customer;
    private List<CartItem> items;
    private double originalAmount;
    private double discountAmount;
    private double finalAmount;
    private String paymentMethod;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public Order(Customer customer, List<CartItem> cartItems, double originalAmount,
                 double discountAmount, double finalAmount, String paymentMethod) {
        this.orderId = nextOrderId++;
        this.customer = customer;
        // Deep copy of cart items
        this.items = new ArrayList<>();
        for (CartItem ci : cartItems) {
            this.items.add(new CartItem(ci));
        }
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.CREATED;
        this.orderDate = LocalDateTime.now();
    }

    public void displayReceipt() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ORDER RECEIPT                                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("  Order ID     : %d%n", orderId);
        System.out.printf("  Customer     : %s (%s)%n", customer.getName(), customer.getEmail());
        System.out.printf("  Order Date   : %s%n", orderDate.format(dtf));
        System.out.printf("  Status       : %s%n", status);
        System.out.printf("  Payment      : %s%n", paymentMethod);
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("  ITEMS:");
        System.out.println("  " + "-".repeat(68));
        System.out.printf("  | %-4s | %-22s | %10s | %-4s | %12s |%n",
                "ID", "Product", "Price", "Qty", "Subtotal");
        System.out.println("  " + "-".repeat(68));
        for (CartItem item : items) {
            System.out.println("  " + item.toString());
        }
        System.out.println("  " + "-".repeat(68));
        System.out.printf("  Subtotal         : \u20B9%.2f%n", originalAmount);
        System.out.printf("  Discount         : -\u20B9%.2f%n", discountAmount);
        System.out.printf("  TOTAL PAYABLE    : \u20B9%.2f%n", finalAmount);
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    // Getters
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<CartItem> getItems() { return items; }
    public double getOriginalAmount() { return originalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalAmount() { return finalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }

    // Setters
    public void setStatus(OrderStatus status) { this.status = status; }
}
