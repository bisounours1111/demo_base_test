package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PriceCalculatorTest {
    @Test
    void shouldCalculateTotalPrice(){
        PriceCalculator priceCalculator = new PriceCalculator();
        double result = priceCalculator.calculateTotalPrice(10.0, 3);
        assertEquals(30.0, result);
    }

    @Test
    void shouldApplyDiscount(){
        PriceCalculator priceCalculator = new PriceCalculator();
        double result = priceCalculator.applyDiscount(100.0, 0.20);
        assertEquals(80.0,result);
    }

    @Test
    void shouldCalculateVat(){
        PriceCalculator priceCalculator = new PriceCalculator();
        double result = priceCalculator.calculateVat(100.0, 0.20);
        assertEquals(20.0, result);
    }

    @Test
    void shouldCalculateVatWithVatRate(){
        PriceCalculator priceCalculator = new PriceCalculator();
        double result = priceCalculator.calculatePriceWithVat(100.0, 0.20);
        assertEquals(120.0, result);
    }

    @Test
    void shouldThrowWhenUnitPriceIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(-10.0, 3));
    }

    @Test
    void shouldThrowWhenQuantityIsInvalid() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(10.0, 0));
    }

    @Test
    void shouldThrowWhenDiscountPriceIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(-100.0, 0.20));
    }

    @Test
    void shouldThrowWhenDiscountRateIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(100.0, -0.20));
    }

    @Test
    void shouldThrowWhenDiscountRateIsTooHigh() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(100.0, 1.5));
    }

    @Test
    void shouldThrowWhenVatPriceIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculateVat(-100.0, 0.20));
    }

    @Test
    void shouldThrowWhenVatRateIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculateVat(100.0, -0.20));
    }

    @Test
    void shouldThrowWhenPriceWithVatPriceIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculatePriceWithVat(-100.0, 0.20));
    }

    @Test
    void shouldThrowWhenPriceWithVatRateIsNegative() {
        PriceCalculator priceCalculator = new PriceCalculator();
        assertThrows(IllegalArgumentException.class,
                () -> priceCalculator.calculatePriceWithVat(100.0, -0.20));
    }
}
