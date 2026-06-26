package com.example.demoapi.model;

import java.math.BigDecimal;

public record Account(String number, String holder, BigDecimal balance) {
}
