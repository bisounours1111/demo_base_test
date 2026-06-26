package com.example.demoapi.service;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.InsufficientFundsException;
import com.example.demoapi.exception.InvalidAmountException;
import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        assertThat(result.balance()).isEqualByComparingTo(new BigDecimal("150.00"));
        verify(repository).findByNumber("ACC001");
        verify(repository).update(updatedAccount);
    }

    @Test
    void shouldThrowInvalidAmountException_whenDepositAmountIsNull() {
        // Act + Assert
        assertThatThrownBy(() -> service.deposit("ACC001", null))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenDepositAmountIsZero() {
        // Act + Assert
        assertThatThrownBy(() -> service.deposit("ACC001", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenDepositAmountIsNegative() {
        // Act + Assert
        assertThatThrownBy(() -> service.deposit("ACC001", new BigDecimal("-10.00")))
                .isInstanceOf(InvalidAmountException.class);
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
        assertThat(result.balance()).isEqualByComparingTo(new BigDecimal("150.00"));
        verify(repository).findByNumber("ACC001");
        verify(repository).update(updatedAccount);
    }

    @Test
    void shouldThrowInvalidAmountException_whenWithdrawAmountIsNull() {
        // Act + Assert
        assertThatThrownBy(() -> service.withdraw("ACC001", null))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenWithdrawAmountIsZero() {
        // Act + Assert
        assertThatThrownBy(() -> service.withdraw("ACC001", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenWithdrawAmountIsNegative() {
        // Act + Assert
        assertThatThrownBy(() -> service.withdraw("ACC001", new BigDecimal("-5.00")))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInsufficientFundsException_whenWithdrawAmountExceedsBalance() {
        // Arrange
        var account = new Account("ACC001", "Alice Dupont", new BigDecimal("30.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(account));

        // Act + Assert
        assertThatThrownBy(() -> service.withdraw("ACC001", new BigDecimal("50.00")))
                .isInstanceOf(InsufficientFundsException.class);
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
    void shouldThrowInvalidAmountException_whenTransferAmountIsNull() {
        // Act + Assert
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", null))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenTransferAmountIsZero() {
        // Act + Assert
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).findByNumber(anyString());
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowInvalidAmountException_whenTransferAmountIsNegative() {
        // Act + Assert
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", new BigDecimal("-20.00")))
                .isInstanceOf(InvalidAmountException.class);
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
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", new BigDecimal("100.00")))
                .isInstanceOf(InsufficientFundsException.class);
        verify(repository).findByNumber("ACC001");
        verify(repository).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowAccountNotFoundException_whenTransferSourceAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("ACC001")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", new BigDecimal("50.00")))
                .isInstanceOf(AccountNotFoundException.class);
        verify(repository).findByNumber("ACC001");
        verify(repository, never()).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }

    @Test
    void shouldThrowAccountNotFoundException_whenTransferTargetAccountDoesNotExist() {
        // Arrange
        var source = new Account("ACC001", "Alice Dupont", new BigDecimal("200.00"));
        when(repository.findByNumber("ACC001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("ACC002")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.transfer("ACC001", "ACC002", new BigDecimal("50.00")))
                .isInstanceOf(AccountNotFoundException.class);
        verify(repository).findByNumber("ACC001");
        verify(repository).findByNumber("ACC002");
        verify(repository, never()).update(any(Account.class));
    }
}
