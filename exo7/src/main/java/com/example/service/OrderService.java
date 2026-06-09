package com.example.service;

import com.example.domain.CustomerProfile;
import com.example.domain.Order;
import com.example.domain.OrderReceipt;
import com.example.repository.ProductRepository;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderReceipt placeOrder(Order order, CustomerProfile customerProfile) {
        return null;
    }
}