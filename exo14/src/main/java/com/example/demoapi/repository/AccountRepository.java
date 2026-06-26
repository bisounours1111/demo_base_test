package com.example.demoapi.repository;

import com.example.demoapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByNumber(String number);

    Optional<Account> findByNumber(String number);

    default Account save(String number, String holder) {
        return save(new Account(number, holder, BigDecimal.ZERO));
    }

    default Account update(Account account) {
        return save(account);
    }
}
