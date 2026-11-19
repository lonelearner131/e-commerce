package com.ecommerce.payment;

import com.ecommerce.exception.InvalidOperationException;

public class UpiPayment implements Payment {

    private final String upiId;

    public UpiPayment(String upiId) {
        if (upiId == null || !upiId.contains("@")) {
            throw new InvalidOperationException("Invalid UPI ID. It must contain '@' (e.g., user@upi).");
        }
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("  [UPI] Processing payment of \u20B9" + String.format("%.2f", amount)
                + " via UPI ID: " + upiId + " ...");
        System.out.println("  [UPI] Payment successful!");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI (" + upiId + ")";
    }
}
