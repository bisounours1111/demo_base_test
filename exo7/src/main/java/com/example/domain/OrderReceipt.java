package com.example.domain;

public class OrderReceipt {
    private String productReference;
    private int quantity;
    private double totalAmount;
    private String confirmationMessage;

    public OrderReceipt(String productReference, int quantity, double totalAmount, String confirmationMessage) {
        this.productReference = productReference;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.confirmationMessage = confirmationMessage;
    }

    public String getProductReference() { return productReference; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
    public String getConfirmationMessage() { return confirmationMessage; }
}