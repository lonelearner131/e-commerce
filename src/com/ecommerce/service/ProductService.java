package com.ecommerce.service;

import com.ecommerce.exception.InvalidOperationException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {

    public static final String[] CATEGORIES = {"Electronics", "Clothing", "Grocery"};

    private Map<Integer, Product> products;

    public ProductService() {
        products = new HashMap<>();
        seedData();
    }

    private void seedData() {
        addProduct(new Electronics(101, "Laptop", 55000, 10, 12));
        addProduct(new Electronics(102, "Smartphone", 25000, 15, 12));
        addProduct(new Clothing(201, "T-Shirt", 999, 25, "L", "UrbanWear"));
        addProduct(new Clothing(202, "Jeans", 1999, 20, "32", "DenimCo"));
        addProduct(new Grocery(301, "Basmati Rice (5kg)", 650, 40, "2027-12-31"));
        addProduct(new Grocery(302, "Cooking Oil (1L)", 180, 50, "2027-06-30"));
    }

    public void addProduct(Product product) {
        if (products.containsKey(product.getProductId())) {
            throw new InvalidOperationException("Product with ID " + product.getProductId() + " already exists.");
        }
        products.put(product.getProductId(), product);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(int id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }
        return product;
    }

    public List<Product> searchProductsByName(String keyword) {
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Product p : products.values()) {
            if (p.getName().toLowerCase().contains(lowerKeyword)) {
                results.add(p);
            }
        }
        return results;
    }

    public void updateProductDetails(int id, String newName) {
        Product product = getProductById(id);
        product.setName(newName);
    }

    public void updateStock(int id, int newStock) {
        Product product = getProductById(id);
        product.setStockQuantity(newStock);
    }

    public void updatePrice(int id, double newPrice) {
        Product product = getProductById(id);
        product.setPrice(newPrice);
    }

    public void removeProduct(int id) {
        Product product = getProductById(id); // throws if not found
        products.remove(id);
    }

    public void reduceStock(int id, int qty) {
        Product product = getProductById(id);
        int newQty = product.getStockQuantity() - qty;
        if (newQty < 0) {
            throw new InvalidOperationException("Cannot reduce stock below 0 for product: " + product.getName());
        }
        product.setStockQuantity(newQty);
    }

    public void restoreStock(int id, int qty) {
        Product product = getProductById(id);
        product.setStockQuantity(product.getStockQuantity() + qty);
    }

    public void displayAllProducts() {
        List<Product> allProducts = getAllProducts();
        if (allProducts.isEmpty()) {
            System.out.println("  No products available.");
            return;
        }
        System.out.println("  " + "-".repeat(95));
        System.out.printf("  | %-4s | %-22s | %-12s | %10s | %-6s | %s%n",
                "ID", "Product Name", "Category", "Price", "Stock", "Details");
        System.out.println("  " + "-".repeat(95));
        for (Product p : allProducts) {
            System.out.println("  " + p.toString());
        }
        System.out.println("  " + "-".repeat(95));
    }
}
