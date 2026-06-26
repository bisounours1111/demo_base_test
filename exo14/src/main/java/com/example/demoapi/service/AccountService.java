package com.example.demoapi.service;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.DuplicateAccountException;
import com.example.demoapi.exception.InsufficientFundsException;
import com.example.demoapi.exception.InvalidAmountException;
import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Account deposit(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = findByNumber(number);
        Account updated = new Account(number, account.holder(), account.balance().add(amount));
        return repository.update(updated);
    }

    public Account withdraw(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = findByNumber(number);
        ensureSufficientFunds(number, account.balance(), amount);
        Account updated = new Account(number, account.holder(), account.balance().subtract(amount));
        return repository.update(updated);
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account source = repository.findByNumber(fromNumber)
                .orElseThrow(() -> new AccountNotFoundException(fromNumber));
        Account target = repository.findByNumber(toNumber)
                .orElseThrow(() -> new AccountNotFoundException(toNumber));
        ensureSufficientFunds(fromNumber, source.balance(), amount);

        repository.update(new Account(
                fromNumber, source.holder(), source.balance().subtract(amount)));
        repository.update(new Account(
                toNumber, target.holder(), target.balance().add(amount)));
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }
    }

    private void ensureSufficientFunds(String accountNumber, BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountNumber);
        }
    }
}
