package com.ecommerce.payment;

public class CashPayment implements Payment {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("  [COD] Cash on Delivery selected for \u20B9" + String.format("%.2f", amount) + ".");
        System.out.println("  [COD] Payment will be collected upon delivery.");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash on Delivery";
    }
}
