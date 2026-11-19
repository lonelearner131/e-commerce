package com.ecommerce.model;

import com.ecommerce.exception.InvalidOperationException;

public abstract class Product {

    private int productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String category;

    public Product(int productId, String name, double price, int stockQuantity, String category) {
        this.productId = productId;
        setName(name);
        setPrice(price);
        setStockQuantity(stockQuantity);
        setCategory(category);
    }

    public abstract String getSpecificDetails();

    // Getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getCategory() { return category; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidOperationException("Product name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidOperationException("Price cannot be negative. Given: " + price);
        }
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new InvalidOperationException("Stock quantity cannot be negative. Given: " + stockQuantity);
        }
        this.stockQuantity = stockQuantity;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidOperationException("Category cannot be null or empty.");
        }
        this.category = category.trim();
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-22s | %-12s | %10s | %-6d | %s",
                productId, name, category,
                "\u20B9" + String.format("%.2f", price),
                stockQuantity, getSpecificDetails());
    }
}
