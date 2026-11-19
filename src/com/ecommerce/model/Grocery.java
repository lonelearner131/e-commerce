package com.ecommerce.model;

public class Grocery extends Product {

    private String expiryDate;

    public Grocery(int productId, String name, double price, int stockQuantity, String expiryDate) {
        super(productId, name, price, stockQuantity, "Grocery");
        this.expiryDate = expiryDate;
    }

    @Override
    public String getSpecificDetails() {
        return "Expiry: " + expiryDate;
    }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
