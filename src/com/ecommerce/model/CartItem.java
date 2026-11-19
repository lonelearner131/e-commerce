package com.ecommerce.model;

public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /** Copy constructor for defensive deep copy in Order. */
    public CartItem(CartItem other) {
        this.product = other.product;      // Product reference is shared (immutable from Order's perspective)
        this.quantity = other.quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-22s | %10s | %-4d | %12s |",
                product.getProductId(), product.getName(),
                "\u20B9" + String.format("%.2f", product.getPrice()),
                quantity,
                "\u20B9" + String.format("%.2f", getSubtotal()));
    }
}
