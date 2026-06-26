package com.example.demoapi.service;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.DuplicateAccountException;
import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    @Test
    void shouldCreateAccount_whenNumberAndHolderAreValid() {
        // Arrange
        var expectedAccount = new Account("ACC001", "Alice Dupont", BigDecimal.ZERO);
        when(repository.existsByNumber("ACC001")).thenReturn(false);
        when(repository.save("ACC001", "Alice Dupont")).thenReturn(expectedAccount);

        // Act
        Account result = service.create("ACC001", "Alice Dupont");

        // Assert
        assertEquals("ACC001", result.number());
        assertEquals("Alice Dupont", result.holder());
        assertEquals(BigDecimal.ZERO, result.balance());
        verify(repository).existsByNumber("ACC001");
        verify(repository).save("ACC001", "Alice Dupont");
    }

    @Test
    void shouldThrowDuplicateAccountException_whenAccountNumberAlreadyExists() {
        // Arrange
        when(repository.existsByNumber("ACC001")).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateAccountException.class,
                () -> service.create("ACC001", "Alice Dupont"));
        verify(repository).existsByNumber("ACC001");
        verify(repository, never()).save(anyString(), anyString());
    }

    @Test
    void shouldReturnAccount_whenFindByNumberAndAccountExists() {
        // Arrange
        var account = new Account("ACC001", "Alice Dupont", new BigDecimal("150.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(account));

        // Act
        Account result = service.findByNumber("ACC001");

        // Assert
        assertEquals("ACC001", result.number());
        assertEquals("Alice Dupont", result.holder());
        assertEquals(new BigDecimal("150.00"), result.balance());
        verify(repository).findByNumber("ACC001");
    }

    @Test
    void shouldThrowAccountNotFoundException_whenAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("ACC999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> service.findByNumber("ACC999"));
        verify(repository).findByNumber("ACC999");
    }

    @Test
    void shouldReturnAllAccounts_whenFindAllIsCalled() {
        // Arrange
        var accounts = List.of(
                new Account("ACC001", "Alice Dupont", BigDecimal.ZERO),
                new Account("ACC002", "Bob Martin", new BigDecimal("500.00"))
        );
        when(repository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ACC001", result.get(0).number());
        assertEquals("ACC002", result.get(1).number());
        verify(repository).findAll();
    }
}
