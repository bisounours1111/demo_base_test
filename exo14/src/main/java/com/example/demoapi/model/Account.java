package com.example.demoapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String number;

    private String holder;

    private BigDecimal balance;

    public String number() {
        return number;
    }

    public String holder() {
        return holder;
    }

    public BigDecimal balance() {
        return balance;
    }
}
