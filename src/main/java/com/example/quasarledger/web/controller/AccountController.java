
package com.example.quasarledger.web.controller;

import com.example.quasarledger.domain.Account;
import com.example.quasarledger.repo.AccountRepository;
import com.example.quasarledger.web.dto.AccountDtos;
import com.example.quasarledger.web.dto.ReportDtos;
import com.example.quasarledger.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accounts;
    private final ReportService reports;

    public AccountController(AccountRepository accounts, ReportService reports) {
        this.accounts = accounts;
        this.reports = reports;
    }

    @PostMapping
    public ResponseEntity<AccountDtos.AccountResponse> create(@Valid @RequestBody AccountDtos.CreateAccountRequest req) {
        Account acc = new Account(req.name, req.type);
        if (req.parentId != null) {
            Account parent = accounts.findById(req.parentId).orElseThrow(() -> new IllegalArgumentException("Conta pai inexistente"));
            acc.setParent(parent);
        }
        Account saved = accounts.save(acc);
        return ResponseEntity.created(URI.create("/api/accounts/" + saved.getId()))
                .body(new AccountDtos.AccountResponse(saved.getId(), saved.getName(), saved.getType(),
                        saved.getParent() != null ? saved.getParent().getId() : null));
    }

    @GetMapping
    public List<AccountDtos.AccountResponse> list() {
        return accounts.findAll().stream()
                .map(a -> new AccountDtos.AccountResponse(a.getId(), a.getName(), a.getType(),
                        a.getParent() != null ? a.getParent().getId() : null))
                .toList();
    }

    @GetMapping("{id}/balance")
    public ReportDtos.BalanceResponse balance(@PathVariable Long id,
                                              @RequestParam(required = false) LocalDate from,
                                              @RequestParam(required = false) LocalDate to) {
        return reports.accountBalance(id, from, to);
    }
}
