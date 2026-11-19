package com.ecommerce.discount;

public interface Discount {
    double calculateDiscount(double totalAmount);
    String getDescription();
}
