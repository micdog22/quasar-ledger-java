
package com.example.quasarledger.web.controller;

import com.example.quasarledger.domain.JournalEntry;
import com.example.quasarledger.service.AccountingService;
import com.example.quasarledger.web.dto.EntryDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private final AccountingService accounting;

    public EntryController(AccountingService accounting) {
        this.accounting = accounting;
    }

    @PostMapping
    public ResponseEntity<EntryDtos.CreateEntryResponse> create(@Valid @RequestBody EntryDtos.CreateEntryRequest req) {
        JournalEntry saved = accounting.createEntry(req);
        return ResponseEntity.created(URI.create("/api/entries/" + saved.getId()))
                .body(new EntryDtos.CreateEntryResponse(saved.getId(), saved.getTimestamp(), saved.getDescription(), saved.getPostings().size()));
    }
}
