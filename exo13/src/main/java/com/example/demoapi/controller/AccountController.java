package com.example.demoapi.controller;

import com.example.demoapi.model.AccountResponse;
import com.example.demoapi.model.AmountRequest;
import com.example.demoapi.model.CreateAccountRequest;
import com.example.demoapi.model.TransferRequest;
import com.example.demoapi.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        var account = service.create(request.number(), request.holder());
        var response = AccountResponse.from(account);

        return ResponseEntity
                .created(URI.create("/accounts/" + response.number()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        var responses = service.findAll()
                .stream()
                .map(AccountResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(AccountResponse.from(service.findByNumber(number)));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        service.transfer(request.fromAccount(), request.toAccount(), request.amount());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(AccountResponse.from(service.deposit(number, request.amount())));
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(AccountResponse.from(service.withdraw(number, request.amount())));
    }
}
