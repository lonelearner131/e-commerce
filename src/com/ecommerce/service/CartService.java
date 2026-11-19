package com.ecommerce.service;

import com.ecommerce.discount.Discount;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Product;

import java.util.List;

public class CartService {

    private ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void addToCart(Customer customer, int productId, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        Product product = productService.getProductById(productId); // throws ProductNotFoundException

        Cart cart = customer.getCart();
        int existingQty = 0;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId() == productId) {
                existingQty = item.getQuantity();
                break;
            }
        }

        int totalRequestedQty = existingQty + qty;
        if (totalRequestedQty > product.getStockQuantity()) {
            throw new InsufficientStockException("Insufficient stock for '" + product.getName()
                    + "'. Available: " + product.getStockQuantity()
                    + ", In cart: " + existingQty + ", Requested: " + qty + ".");
        }

        // Update existing item or add new
        boolean found = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId() == productId) {
                item.setQuantity(totalRequestedQty);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.getItems().add(new CartItem(product, qty));
        }
    }

    public void removeFromCart(Customer customer, int productId) {
        Cart cart = customer.getCart();
        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getProductId() == productId);
        if (!removed) {
            throw new ProductNotFoundException("Product with ID " + productId + " is not in your cart.");
        }
    }

    public void updateQuantity(Customer customer, int productId, int newQty) {
        if (newQty == 0) {
            removeFromCart(customer, productId);
            return;
        }
        if (newQty < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Cart cart = customer.getCart();
        CartItem targetItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId() == productId) {
                targetItem = item;
                break;
            }
        }
        if (targetItem == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " is not in your cart.");
        }

        Product product = productService.getProductById(productId);
        if (newQty > product.getStockQuantity()) {
            throw new InsufficientStockException("Insufficient stock for '" + product.getName()
                    + "'. Available: " + product.getStockQuantity() + ", Requested: " + newQty + ".");
        }
        targetItem.setQuantity(newQty);
    }

    public void applyDiscount(Customer customer, Discount discount) {
        customer.getCart().setAppliedDiscount(discount);
    }

    public void displayCart(Customer customer) {
        Cart cart = customer.getCart();
        if (cart.isEmpty()) {
            System.out.println("\n  Your cart is empty.");
            return;
        }

        List<CartItem> items = cart.getItems();
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                      YOUR SHOPPING CART                          ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("  " + "-".repeat(68));
        System.out.printf("  | %-4s | %-22s | %10s | %-4s | %12s |%n",
                "ID", "Product", "Price", "Qty", "Subtotal");
        System.out.println("  " + "-".repeat(68));
        for (CartItem item : items) {
            System.out.println("  " + item.toString());
        }
        System.out.println("  " + "-".repeat(68));
        System.out.printf("  Subtotal         : \u20B9%.2f%n", cart.getSubtotal());
        System.out.printf("  Discount         : %s%n", cart.getAppliedDiscount().getDescription());
        System.out.printf("  Discount Amount  : -\u20B9%.2f%n", cart.getDiscountAmount());
        System.out.printf("  TOTAL PAYABLE    : \u20B9%.2f%n", cart.getFinalTotal());
        System.out.println("  ╚══════════════════════════════════════════════════════════════════╝");
    }
}
