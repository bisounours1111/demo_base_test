package com.example;

public class PriceCalculator {
    public double calculateTotalPrice(double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

    public double applyDiscount(double price, double discountRate) {
        return price - (price * discountRate);
    }

    public double calculateVat(double price, double vatRate) {
        return price * vatRate;
    }

    public double calculatePriceWithVat(double price, double vatRate) {
        return price + (price * vatRate);
    }
}
