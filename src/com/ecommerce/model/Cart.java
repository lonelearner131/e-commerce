package com.ecommerce.model;

import com.ecommerce.discount.Discount;
import com.ecommerce.discount.NoDiscount;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items;
    private Discount appliedDiscount;

    public Cart() {
        this.items = new ArrayList<>();
        this.appliedDiscount = new NoDiscount();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double getDiscountAmount() {
        return appliedDiscount.calculateDiscount(getSubtotal());
    }

    public double getFinalTotal() {
        double finalTotal = getSubtotal() - getDiscountAmount();
        return Math.max(finalTotal, 0);
    }

    public Discount getAppliedDiscount() {
        return appliedDiscount;
    }

    public void setAppliedDiscount(Discount discount) {
        this.appliedDiscount = discount;
    }

    public void clear() {
        items.clear();
        appliedDiscount = new NoDiscount();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
