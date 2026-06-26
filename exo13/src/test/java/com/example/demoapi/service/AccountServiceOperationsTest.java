package com.example.demoapi.service;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.InsufficientFundsException;
import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceOperationsTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    // --- Dépôt ---

    @Test
    void shouldDepositSuccessfully_whenAmountIsPositive() {
        // Arrange
        var account = new Account("ACC001", "Alice Dupont", new BigDecimal("100.00"));
        var updatedAccount = new Account("ACC001", "Alice Dupont", new BigDecimal("150.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(account));
        when(repository.update(updatedAccount)).thenReturn(updatedAccount);

        // Act
        Account result = service.deposit("ACC001", new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), result.balance());
        verify(repository).findByNumber("ACC001");
        verify(repository).update(updatedAccount);
    }

    @Test
    void shouldThrowException_whenDepositAmountIsZero() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.deposit("ACC001", BigDecimal.ZERO));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowException_whenDepositAmountIsNegative() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.deposit("ACC001", new BigDecimal("-10.00")));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    // --- Retrait ---

    @Test
    void shouldWithdrawSuccessfully_whenBalanceIsSufficient() {
        // Arrange
        var account = new Account("ACC001", "Alice Dupont", new BigDecimal("200.00"));
        var updatedAccount = new Account("ACC001", "Alice Dupont", new BigDecimal("150.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(account));
        when(repository.update(updatedAccount)).thenReturn(updatedAccount);

        // Act
        Account result = service.withdraw("ACC001", new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), result.balance());
        verify(repository).findByNumber("ACC001");
        verify(repository).update(updatedAccount);
    }

    @Test
    void shouldThrowException_whenWithdrawAmountIsZero() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.withdraw("ACC001", BigDecimal.ZERO));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowException_whenWithdrawAmountIsNegative() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.withdraw("ACC001", new BigDecimal("-5.00")));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInsufficientFundsException_whenWithdrawAmountExceedsBalance() {
        // Arrange
        var account = new Account("ACC001", "Alice Dupont", new BigDecimal("30.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(account));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw("ACC001", new BigDecimal("50.00")));
        verify(repository).findByNumber("ACC001");
        verify(repository, never()).update(any(Account.class));
    }

    // --- Virement ---

    @Test
    void shouldTransferSuccessfully_whenBalanceIsSufficient() {
        // Arrange
        var source = new Account("ACC001", "Alice Dupont", new BigDecimal("200.00"));
        var target = new Account("ACC002", "Bob Martin", new BigDecimal("50.00"));
        var updatedSource = new Account("ACC001", "Alice Dupont", new BigDecimal("125.00"));
        var updatedTarget = new Account("ACC002", "Bob Martin", new BigDecimal("125.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("ACC002")).thenReturn(Optional.of(target));
        when(repository.update(updatedSource)).thenReturn(updatedSource);
        when(repository.update(updatedTarget)).thenReturn(updatedTarget);

        // Act
        service.transfer("ACC001", "ACC002", new BigDecimal("75.00"));

        // Assert
        verify(repository).findByNumber("ACC001");
        verify(repository).findByNumber("ACC002");
        verify(repository).update(updatedSource);
        verify(repository).update(updatedTarget);
    }

    @Test
    void shouldThrowException_whenTransferAmountIsZero() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer("ACC001", "ACC002", BigDecimal.ZERO));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowException_whenTransferAmountIsNegative() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer("ACC001", "ACC002", new BigDecimal("-20.00")));
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInsufficientFundsException_whenTransferAmountExceedsSourceBalance() {
        // Arrange
        var source = new Account("ACC001", "Alice Dupont", new BigDecimal("40.00"));
        var target = new Account("ACC002", "Bob Martin", new BigDecimal("100.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("ACC002")).thenReturn(Optional.of(target));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer("ACC001", "ACC002", new BigDecimal("100.00")));
        verify(repository).findByNumber("ACC001");
        verify(repository).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowAccountNotFoundException_whenSourceAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("ACC001")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer("ACC001", "ACC002", new BigDecimal("50.00")));
        verify(repository).findByNumber("ACC001");
        verify(repository, never()).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowAccountNotFoundException_whenTargetAccountDoesNotExist() {
        // Arrange
        var source = new Account("ACC001", "Alice Dupont", new BigDecimal("200.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("ACC002")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer("ACC001", "ACC002", new BigDecimal("50.00")));
        verify(repository).findByNumber("ACC001");
        verify(repository).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }
}
