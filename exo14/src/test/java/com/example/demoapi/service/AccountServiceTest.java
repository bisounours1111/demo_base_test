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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void shouldCreateAccount_withInitialBalanceZero() {
        // Arrange
        var expectedAccount = new Account("ACC001", "Alice Dupont", BigDecimal.ZERO);
        when(repository.existsByNumber("ACC001")).thenReturn(false);
        when(repository.save("ACC001", "Alice Dupont")).thenReturn(expectedAccount);

        // Act
        Account result = service.create("ACC001", "Alice Dupont");

        // Assert
        assertThat(result.number()).isEqualTo("ACC001");
        assertThat(result.holder()).isEqualTo("Alice Dupont");
        assertThat(result.balance()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(repository).existsByNumber("ACC001");
        verify(repository).save("ACC001", "Alice Dupont");
    }

    @Test
    void shouldThrowDuplicateAccountException_whenAccountNumberAlreadyExists() {
        // Arrange
        when(repository.existsByNumber("ACC001")).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> service.create("ACC001", "Alice Dupont"))
                .isInstanceOf(DuplicateAccountException.class);
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
        assertThat(result.number()).isEqualTo("ACC001");
        assertThat(result.holder()).isEqualTo("Alice Dupont");
        assertThat(result.balance()).isEqualByComparingTo(new BigDecimal("150.00"));
        verify(repository).findByNumber("ACC001");
    }

    @Test
    void shouldThrowAccountNotFoundException_whenAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("ACC999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.findByNumber("ACC999"))
                .isInstanceOf(AccountNotFoundException.class);
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
        assertThat(result).hasSize(2);
        assertThat(result.get(0).number()).isEqualTo("ACC001");
        assertThat(result.get(1).number()).isEqualTo("ACC002");
        verify(repository).findAll();
    }
}
