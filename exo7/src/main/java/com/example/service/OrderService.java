package com.example.service;

import com.example.domain.CustomerProfile;
import com.example.domain.Order;
import com.example.domain.OrderReceipt;
import com.example.domain.Product;
import com.example.exception.OrderRefusedException;
import com.example.repository.ProductRepository;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderReceipt placeOrder(Order order, CustomerProfile customerProfile) {
        Product product = productRepository.findByReference(order.getProductReference())
                .orElseThrow(() -> new OrderRefusedException("Product not found"));

        if (product.getAvailableStock() < order.getQuantity()) {
            throw new OrderRefusedException("Insufficient stock");
        }

        double baseAmount = product.getUnitPrice() * order.getQuantity();
        double discountRate = switch (customerProfile) {
            case STANDARD -> 0.0;
            case PREMIUM -> 0.10;
            case VIP -> 0.20;
        };
        double totalAmount = baseAmount * (1 - discountRate);

        product.setAvailableStock(product.getAvailableStock() - order.getQuantity());

        return new OrderReceipt(
                product.getReference(),
                order.getQuantity(),
                totalAmount,
                "Order confirmed for " + order.getCustomerEmail() + " - " + product.getName()
        );
    }
}