package com.example.domain;

public class Product {
    private String reference;
    private String name;
    private double unitPrice;
    private int availableStock;

    public Product(String reference, String name, double unitPrice, int availableStock) {
        this.reference = reference;
        this.name = name;
        this.unitPrice = unitPrice;
        this.availableStock = availableStock;
    }

    public String getReference() { return reference; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }
}