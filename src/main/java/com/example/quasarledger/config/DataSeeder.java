
package com.example.quasarledger.config;

import com.example.quasarledger.domain.*;
import com.example.quasarledger.repo.AccountRepository;
import com.example.quasarledger.service.AccountingService;
import com.example.quasarledger.web.dto.EntryDtos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(AccountRepository accounts, AccountingService accounting) {
        return args -> {
            if (accounts.count() > 0) return;

            Account caixa = accounts.save(new Account("Caixa", AccountType.ASSET));
            Account banco = accounts.save(new Account("Banco", AccountType.ASSET));
            Account patrimonio = accounts.save(new Account("Patrimonio Liquido", AccountType.EQUITY));
            Account receitas = accounts.save(new Account("Receitas Salariais", AccountType.INCOME));
            Account despesas = accounts.save(new Account("Despesas Mercado", AccountType.EXPENSE));

            // Abertura: Debita Caixa 1000 / Credita PL 1000
            EntryDtos.CreateEntryRequest r1 = new EntryDtos.CreateEntryRequest();
            r1.description = "Abertura de caixa";
            r1.postings = List.of(
                posting(caixa.getId(), PostingType.DEBIT, new BigDecimal("1000.00")),
                posting(patrimonio.getId(), PostingType.CREDIT, new BigDecimal("1000.00"))
            );
            accounting.createEntry(r1);

            // Salário: Debita Banco 5000 / Credita Receita 5000
            EntryDtos.CreateEntryRequest r2 = new EntryDtos.CreateEntryRequest();
            r2.description = "Recebimento de salário";
            r2.postings = List.of(
                posting(banco.getId(), PostingType.DEBIT, new BigDecimal("5000.00")),
                posting(receitas.getId(), PostingType.CREDIT, new BigDecimal("5000.00"))
            );
            accounting.createEntry(r2);

            // Compras: Debita Despesa 300 / Credita Caixa 300
            EntryDtos.CreateEntryRequest r3 = new EntryDtos.CreateEntryRequest();
            r3.description = "Compras de mercado";
            r3.postings = List.of(
                posting(despesas.getId(), PostingType.DEBIT, new BigDecimal("300.00")),
                posting(caixa.getId(), PostingType.CREDIT, new BigDecimal("300.00"))
            );
            accounting.createEntry(r3);
        };
    }

    private EntryDtos.PostingRequest posting(Long accountId, PostingType type, BigDecimal amount) {
        EntryDtos.PostingRequest p = new EntryDtos.PostingRequest();
        p.accountId = accountId; p.type = type; p.amount = amount;
        return p;
    }
}
