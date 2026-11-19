package com.ecommerce.model;

import com.ecommerce.enums.Role;

public abstract class User {

    private static int nextId = 1;

    private int userId;
    private String name;
    private String email;
    private String password;
    private Role role;

    public User(String name, String email, String password, Role role) {
        this.userId = nextId++;
        setName(name);
        setEmail(email);
        setPassword(password);
        this.role = role;
    }

    public User(int userId, String name, String email, String password, Role role) {
        this.userId = userId;
        if (userId >= nextId) {
            nextId = userId + 1;
        }
        setName(name);
        setEmail(email);
        setPassword(password);
        this.role = role;
    }

    public abstract void displayProfile();

    // Getters
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
        this.password = password;
    }
}
