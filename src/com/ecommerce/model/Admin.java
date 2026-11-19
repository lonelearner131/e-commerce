package com.ecommerce.model;

import com.ecommerce.enums.Role;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password, Role.ADMIN);
    }

    public Admin(int userId, String name, String email, String password) {
        super(userId, name, email, password, Role.ADMIN);
    }

    @Override
    public void displayProfile() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           ADMIN PROFILE              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  User ID  : %-24d ║%n", getUserId());
        System.out.printf("║  Name     : %-24s ║%n", getName());
        System.out.printf("║  Email    : %-24s ║%n", getEmail());
        System.out.println("╚══════════════════════════════════════╝");
    }
}
