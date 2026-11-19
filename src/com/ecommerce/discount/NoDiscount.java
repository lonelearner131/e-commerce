package com.ecommerce.discount;

public class NoDiscount implements Discount {

    @Override
    public double calculateDiscount(double totalAmount) {
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "No Discount Applied";
    }
}
