package com.example;

public class PriceCalculator {

    public double calculateTotalPrice(double unitPrice, int quantity) {
        if (unitPrice < 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return unitPrice * quantity;
    }

    public double applyDiscount(double price, double discountRate) {
        if (price < 0 || discountRate < 0 || discountRate > 1) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return price - (price * discountRate);
    }

    public double calculateVat(double price, double vatRate) {
        if (price < 0 || vatRate < 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return price * vatRate;
    }

    public double calculatePriceWithVat(double price, double vatRate) {
        if (price < 0 || vatRate < 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return price + (price * vatRate);
    }
}
