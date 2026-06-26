package com.example.demoapi.model;

public record AccountResponse(String number, String holder, java.math.BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.number(), account.holder(), account.balance());
    }
}
