package com.ecommerce.service;

import com.ecommerce.payment.Payment;

public class PaymentService {

    public boolean executePayment(Payment payment, double amount) {
        return payment.processPayment(amount);
    }
}
