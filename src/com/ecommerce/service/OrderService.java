package com.ecommerce.service;

import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidOperationException;
import com.ecommerce.model.*;
import com.ecommerce.payment.Payment;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private List<Order> orders;
    private ProductService productService;
    private PaymentService paymentService;

    public OrderService(ProductService productService, PaymentService paymentService) {
        this.orders = new ArrayList<>();
        this.productService = productService;
        this.paymentService = paymentService;
    }

    public Order placeOrder(Customer customer, Payment payment) {
        Cart cart = customer.getCart();

        if (cart.isEmpty()) {
            throw new InvalidOperationException("Cannot place order: your cart is empty.");
        }

        // Re-verify stock for every cart item
        for (CartItem item : cart.getItems()) {
            Product product = productService.getProductById(item.getProduct().getProductId());
            if (item.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException("Insufficient stock for '" + product.getName()
                        + "'. Available: " + product.getStockQuantity()
                        + ", In cart: " + item.getQuantity() + ".");
            }
        }

        double originalAmount = cart.getSubtotal();
        double discountAmount = cart.getDiscountAmount();
        double finalAmount = cart.getFinalTotal();

        // Create order with CREATED status
        Order order = new Order(customer, cart.getItems(), originalAmount,
                discountAmount, finalAmount, payment.getPaymentMethodName());

        // Process payment
        boolean paymentSuccess = paymentService.executePayment(payment, finalAmount);
        if (!paymentSuccess) {
            throw new InvalidOperationException("Payment failed. Order not placed.");
        }

        // On payment success: reduce stock, confirm, store, clear cart
        for (CartItem item : cart.getItems()) {
            productService.reduceStock(item.getProduct().getProductId(), item.getQuantity());
        }
        order.setStatus(OrderStatus.CONFIRMED);
        orders.add(order);
        cart.clear();

        return order;
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getUserId() == customer.getUserId()) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public void cancelOrder(int orderId, Customer customer) {
        Order order = findOrderById(orderId);

        if (order.getCustomer().getUserId() != customer.getUserId()) {
            throw new InvalidOperationException("You can only cancel your own orders.");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOperationException("Cannot cancel a delivered order (Order #" + orderId + ").");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Order #" + orderId + " is already cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        // Restore stock
        for (CartItem item : order.getItems()) {
            productService.restoreStock(item.getProduct().getProductId(), item.getQuantity());
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot update a cancelled order (Order #" + orderId + ").");
        }
        if (currentStatus == OrderStatus.DELIVERED) {
            throw new InvalidOperationException("Order #" + orderId + " is already delivered. No further updates.");
        }

        // Enforce valid forward transitions
        switch (newStatus) {
            case SHIPPED:
                if (currentStatus != OrderStatus.CONFIRMED) {
                    throw new InvalidOperationException("Order #" + orderId
                            + " must be CONFIRMED before shipping. Current: " + currentStatus + ".");
                }
                break;
            case DELIVERED:
                if (currentStatus != OrderStatus.SHIPPED) {
                    throw new InvalidOperationException("Order #" + orderId
                            + " must be SHIPPED before delivery. Current: " + currentStatus + ".");
                }
                break;
            case CANCELLED:
                if (currentStatus == OrderStatus.DELIVERED) {
                    throw new InvalidOperationException("Cannot cancel a delivered order.");
                }
                order.setStatus(OrderStatus.CANCELLED);
                for (CartItem item : order.getItems()) {
                    productService.restoreStock(item.getProduct().getProductId(), item.getQuantity());
                }
                return;
            default:
                throw new InvalidOperationException("Invalid status transition to " + newStatus + ".");
        }

        order.setStatus(newStatus);
    }

    private Order findOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        throw new InvalidOperationException("Order with ID " + orderId + " not found.");
    }
}
