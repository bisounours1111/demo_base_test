package com.example.demoapi.repository;

import com.example.demoapi.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    boolean existsByNumber(String number);

    Account save(String number, String holder);

    Optional<Account> findByNumber(String number);

    List<Account> findAll();

    void deleteAll();
}
