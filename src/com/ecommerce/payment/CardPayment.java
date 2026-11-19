package com.ecommerce.payment;

import com.ecommerce.exception.InvalidOperationException;

public class CardPayment implements Payment {

    private final String cardNumber;
    private final String cardHolderName;
    private final String cvv;

    public CardPayment(String cardNumber, String cardHolderName, String cvv) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new InvalidOperationException("Invalid card number. Must be exactly 16 digits.");
        }
        if (cardHolderName == null || cardHolderName.isEmpty()) {
            throw new InvalidOperationException("Card holder name cannot be empty.");
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            throw new InvalidOperationException("Invalid CVV. Must be exactly 3 digits.");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment(double amount) {
        String maskedCard = "XXXX-XXXX-XXXX-" + cardNumber.substring(12);
        System.out.println("  [CARD] Processing payment of \u20B9" + String.format("%.2f", amount)
                + " via Card: " + maskedCard + " ...");
        System.out.println("  [CARD] Payment successful!");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Card (XXXX-" + cardNumber.substring(12) + ")";
    }
}
