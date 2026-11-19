package com.ecommerce.model;

public class Clothing extends Product {

    private String size;
    private String brand;

    public Clothing(int productId, String name, double price, int stockQuantity, String size, String brand) {
        super(productId, name, price, stockQuantity, "Clothing");
        this.size = size;
        this.brand = brand;
    }

    @Override
    public String getSpecificDetails() {
        return "Size: " + size + ", Brand: " + brand;
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
