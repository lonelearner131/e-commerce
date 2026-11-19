package com.ecommerce.model;

public class Electronics extends Product {

    private int warrantyMonths;

    public Electronics(int productId, String name, double price, int stockQuantity, int warrantyMonths) {
        super(productId, name, price, stockQuantity, "Electronics");
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getSpecificDetails() {
        return "Warranty: " + warrantyMonths + " months";
    }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }
}
