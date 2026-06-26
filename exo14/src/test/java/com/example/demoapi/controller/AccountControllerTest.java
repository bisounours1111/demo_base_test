package com.example.demoapi.controller;

import com.example.demoapi.exception.AccountNotFoundException;
import com.example.demoapi.exception.DuplicateAccountException;
import com.example.demoapi.exception.InsufficientFundsException;
import com.example.demoapi.model.Account;
import com.example.demoapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(ApiExceptionHandler.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService service;

    @Test
    void shouldReturnCreated_whenCreateAccountIsValid() throws Exception {
        // Arrange
        when(service.create("ACC001", "Alice Dupont"))
                .thenReturn(new Account("ACC001", "Alice Dupont", BigDecimal.ZERO));

        // Act + Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"number":"ACC001","holder":"Alice Dupont"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/accounts/ACC001"))
                .andExpect(jsonPath("$.number").value("ACC001"))
                .andExpect(jsonPath("$.holder").value("Alice Dupont"))
                .andExpect(jsonPath("$.balance").value(0));

        verify(service).create("ACC001", "Alice Dupont");
    }

    @Test
    void shouldReturnConflict_whenAccountNumberAlreadyExists() throws Exception {
        // Arrange
        when(service.create("ACC001", "Alice Dupont"))
                .thenThrow(new DuplicateAccountException("ACC001"));

        // Act + Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"number":"ACC001","holder":"Alice Dupont"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnOk_whenGetAllAccounts() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(List.of(
                new Account("ACC001", "Alice Dupont", BigDecimal.ZERO),
                new Account("ACC002", "Bob Martin", new BigDecimal("500.00"))
        ));

        // Act + Assert
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].number").value("ACC001"))
                .andExpect(jsonPath("$[1].number").value("ACC002"));

        verify(service).findAll();
    }

    @Test
    void shouldReturnOk_whenGetAccountByNumber() throws Exception {
        // Arrange
        when(service.findByNumber("ACC001"))
                .thenReturn(new Account("ACC001", "Alice Dupont", new BigDecimal("150.00")));

        // Act + Assert
        mockMvc.perform(get("/accounts/ACC001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("ACC001"))
                .andExpect(jsonPath("$.holder").value("Alice Dupont"))
                .andExpect(jsonPath("$.balance").value(150.00));

        verify(service).findByNumber("ACC001");
    }

    @Test
    void shouldReturnNotFound_whenAccountDoesNotExist() throws Exception {
        // Arrange
        when(service.findByNumber("ACC999"))
                .thenThrow(new AccountNotFoundException("ACC999"));

        // Act + Assert
        mockMvc.perform(get("/accounts/ACC999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No account found with number ACC999"));
    }

    @Test
    void shouldReturnOk_whenDepositIsValid() throws Exception {
        // Arrange
        when(service.deposit(eq("ACC001"), eq(new BigDecimal("50.00"))))
                .thenReturn(new Account("ACC001", "Alice Dupont", new BigDecimal("150.00")));

        // Act + Assert
        mockMvc.perform(post("/accounts/ACC001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":50.00}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.00));

        verify(service).deposit("ACC001", new BigDecimal("50.00"));
    }

    @Test
    void shouldReturnOk_whenWithdrawIsValid() throws Exception {
        // Arrange
        when(service.withdraw(eq("ACC001"), eq(new BigDecimal("30.00"))))
                .thenReturn(new Account("ACC001", "Alice Dupont", new BigDecimal("70.00")));

        // Act + Assert
        mockMvc.perform(post("/accounts/ACC001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":30.00}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(70.00));

        verify(service).withdraw("ACC001", new BigDecimal("30.00"));
    }

    @Test
    void shouldReturnNoContent_whenTransferIsValid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromAccount": "ACC001",
                                  "toAccount": "ACC002",
                                  "amount": 75.00
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(service).transfer("ACC001", "ACC002", new BigDecimal("75.00"));
    }

    @Test
    void shouldReturnBadRequest_whenDepositAmountIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/accounts/ACC001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Amount must be strictly positive"));

        verify(service, never()).deposit(any(), any());
    }

    @Test
    void shouldReturnBadRequest_whenInsufficientFundsOnWithdraw() throws Exception {
        // Arrange
        when(service.withdraw("ACC001", new BigDecimal("500.00")))
                .thenThrow(new InsufficientFundsException("ACC001"));

        // Act + Assert
        mockMvc.perform(post("/accounts/ACC001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":500.00}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Insufficient funds on account ACC001"));
    }
}
