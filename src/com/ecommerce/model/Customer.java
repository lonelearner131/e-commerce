package com.ecommerce.model;

import com.ecommerce.enums.Role;

public class Customer extends User {

    private String address;
    private Cart cart;

    public Customer(String name, String email, String password, String address) {
        super(name, email, password, Role.CUSTOMER);
        setAddress(address);
        this.cart = new Cart();
    }

    public Customer(int userId, String name, String email, String password, String address) {
        super(userId, name, email, password, Role.CUSTOMER);
        setAddress(address);
        this.cart = new Cart();
    }

    @Override
    public void displayProfile() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         CUSTOMER PROFILE             ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  User ID  : %-24d ║%n", getUserId());
        System.out.printf("║  Name     : %-24s ║%n", getName());
        System.out.printf("║  Email    : %-24s ║%n", getEmail());
        System.out.printf("║  Address  : %-24s ║%n", address);
        System.out.println("╚══════════════════════════════════════╝");
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.address = address.trim();
    }

    public Cart getCart() { return cart; }
}
