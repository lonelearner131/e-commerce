package com.ecommerce.discount;

public class PercentageDiscount implements Discount {

    private final String code;
    private final double percentage;

    public PercentageDiscount(String code, double percentage) {
        this.code = code;
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(double totalAmount) {
        return totalAmount * (percentage / 100.0);
    }

    @Override
    public String getDescription() {
        return "Discount Code: " + code + " (" + percentage + "% off)";
    }

    public String getCode() {
        return code;
    }

    public double getPercentage() {
        return percentage;
    }
}
