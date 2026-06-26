package com.example.demoapi.service;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.DuplicateAccountException;
import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(String number, String holder) {
        if (repository.existsByNumber(number)) {
            throw new DuplicateAccountException(number);
        }
        return repository.save(number, holder);
    }

    public Account findByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> findAll() {
        return repository.findAll();
    }
}
