
package com.example.quasarledger.service;

import com.example.quasarledger.domain.*;
import com.example.quasarledger.repo.AccountRepository;
import com.example.quasarledger.repo.JournalEntryRepository;
import com.example.quasarledger.repo.PostingRepository;
import com.example.quasarledger.web.dto.EntryDtos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountingService {

    private final AccountRepository accounts;
    private final JournalEntryRepository entries;
    private final PostingRepository postings;

    public AccountingService(AccountRepository accounts, JournalEntryRepository entries, PostingRepository postings) {
        this.accounts = accounts;
        this.entries = entries;
        this.postings = postings;
    }

    @Transactional
    public JournalEntry createEntry(EntryDtos.CreateEntryRequest req) {
        if (req.postings == null || req.postings.size() < 2) {
            throw new IllegalArgumentException("Um lançamento precisa de pelo menos dois lançamentos (postings).");
        }

        BigDecimal deb = BigDecimal.ZERO;
        BigDecimal cred = BigDecimal.ZERO;

        JournalEntry entry = new JournalEntry(
                req.timestamp != null ? req.timestamp : LocalDateTime.now(),
                req.description
        );

        for (EntryDtos.PostingRequest pr : req.postings) {
            Account acc = accounts.findById(pr.accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Conta inexistente: id=" + pr.accountId));
            if (pr.amount == null || pr.amount.signum() <= 0) {
                throw new IllegalArgumentException("Valor do lançamento deve ser positivo.");
            }
            Posting p = new Posting(acc, pr.type, pr.amount);
            entry.addPosting(p);
            if (pr.type == PostingType.DEBIT) deb = deb.add(pr.amount);
            else cred = cred.add(pr.amount);
        }

        if (deb.compareTo(cred) != 0) {
            throw new IllegalArgumentException("Lançamento não balanceado: débitos=" + deb + " créditos=" + cred);
        }

        return entries.save(entry);
    }
}
