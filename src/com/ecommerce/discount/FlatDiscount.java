package com.ecommerce.discount;

public class FlatDiscount implements Discount {

    private final String code;
    private final double flatAmount;
    private final double minOrderAmount;

    public FlatDiscount(String code, double flatAmount, double minOrderAmount) {
        this.code = code;
        this.flatAmount = flatAmount;
        this.minOrderAmount = minOrderAmount;
    }

    @Override
    public double calculateDiscount(double totalAmount) {
        if (totalAmount >= minOrderAmount) {
            return flatAmount;
        }
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Discount Code: " + code + " (Flat \u20B9" + String.format("%.0f", flatAmount)
                + " off on orders >= \u20B9" + String.format("%.0f", minOrderAmount) + ")";
    }

    public String getCode() {
        return code;
    }

    public double getFlatAmount() {
        return flatAmount;
    }

    public double getMinOrderAmount() {
        return minOrderAmount;
    }
}
