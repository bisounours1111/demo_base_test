package com.example.domain;

public class Order {
    private String customerEmail;
    private String productReference;
    private int quantity;

    public Order(String customerEmail, String productReference, int quantity) {
        this.customerEmail = customerEmail;
        this.productReference = productReference;
        this.quantity = quantity;
    }

    public String getCustomerEmail() { return customerEmail; }
    public String getProductReference() { return productReference; }
    public int getQuantity() { return quantity; }
}