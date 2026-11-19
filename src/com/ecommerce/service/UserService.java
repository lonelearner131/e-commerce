package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Customer;
import com.ecommerce.exception.InvalidOperationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {

    private List<Customer> customers;
    private List<Admin> admins;
    private Set<String> registeredEmails;

    public UserService() {
        customers = new ArrayList<>();
        admins = new ArrayList<>();
        registeredEmails = new HashSet<>();
        seedData();
    }

    private void seedData() {
        // Default Admin
        Admin defaultAdmin = new Admin(100, "Admin", "admin@shop.com", "admin123");
        admins.add(defaultAdmin);
        registeredEmails.add(defaultAdmin.getEmail());

        // Sample Customer
        Customer sampleCustomer = new Customer(200, "John Doe", "john@gmail.com", "john123", "123 Main St");
        customers.add(sampleCustomer);
        registeredEmails.add(sampleCustomer.getEmail());
    }

    public Customer registerCustomer(String name, String email, String password, String address) {
        String normalizedEmail = email.trim().toLowerCase();
        if (registeredEmails.contains(normalizedEmail)) {
            throw new InvalidOperationException("Email '" + normalizedEmail + "' is already registered.");
        }
        Customer customer = new Customer(name, normalizedEmail, password, address);
        customers.add(customer);
        registeredEmails.add(normalizedEmail);
        return customer;
    }

    public Customer loginCustomer(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        for (Customer c : customers) {
            if (c.getEmail().equals(normalizedEmail) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    public Admin loginAdmin(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        for (Admin a : admins) {
            if (a.getEmail().equals(normalizedEmail) && a.getPassword().equals(password)) {
                return a;
            }
        }
        return null;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Admin> getAdmins() {
        return admins;
    }
}
