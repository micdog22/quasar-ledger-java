
package com.example.quasarledger;

import com.example.quasarledger.domain.*;
import com.example.quasarledger.repo.AccountRepository;
import com.example.quasarledger.service.AccountingService;
import com.example.quasarledger.web.dto.EntryDtos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
class AccountingServiceTests {

    @Autowired AccountingService accounting;
    @Autowired AccountRepository accounts;

    @Test
    void rejectsUnbalancedEntry() {
        Account a1 = accounts.save(new Account("Teste A", AccountType.ASSET));
        Account a2 = accounts.save(new Account("Teste B", AccountType.EQUITY));

        EntryDtos.CreateEntryRequest req = new EntryDtos.CreateEntryRequest();
        req.description = "Lançamento inválido";
        req.postings = List.of(
                posting(a1.getId(), PostingType.DEBIT, new BigDecimal("100.00")),
                posting(a2.getId(), PostingType.CREDIT, new BigDecimal("90.00"))
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> accounting.createEntry(req));
    }

    @Test
    void acceptsBalancedEntry() {
        Account a1 = accounts.save(new Account("Teste C", AccountType.ASSET));
        Account a2 = accounts.save(new Account("Teste D", AccountType.EQUITY));

        EntryDtos.CreateEntryRequest req = new EntryDtos.CreateEntryRequest();
        req.description = "Lançamento válido";
        req.postings = List.of(
                posting(a1.getId(), PostingType.DEBIT, new BigDecimal("150.00")),
                posting(a2.getId(), PostingType.CREDIT, new BigDecimal("150.00"))
        );
        var entry = accounting.createEntry(req);
        Assertions.assertNotNull(entry.getId());
        Assertions.assertEquals(2, entry.getPostings().size());
    }

    private EntryDtos.PostingRequest posting(Long accountId, PostingType type, BigDecimal amount) {
        EntryDtos.PostingRequest p = new EntryDtos.PostingRequest();
        p.accountId = accountId; p.type = type; p.amount = amount;
        return p;
    }
}
