
package com.example.quasarledger.service;

import com.example.quasarledger.domain.*;
import com.example.quasarledger.repo.AccountRepository;
import com.example.quasarledger.repo.PostingRepository;
import com.example.quasarledger.web.dto.ReportDtos;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final AccountRepository accounts;
    private final PostingRepository postings;

    public ReportService(AccountRepository accounts, PostingRepository postings) {
        this.accounts = accounts;
        this.postings = postings;
    }

    public ReportDtos.BalanceResponse accountBalance(Long accountId, LocalDate from, LocalDate to) {
        LocalDateTime start = from != null ? from.atStartOfDay() : LocalDate.of(1970,1,1).atStartOfDay();
        LocalDateTime end   = to   != null ? to.atTime(LocalTime.MAX) : LocalDate.of(2999,12,31).atTime(LocalTime.MAX);

        BigDecimal deb = BigDecimal.ZERO;
        BigDecimal cred = BigDecimal.ZERO;
        for (Posting p : postings.findByAccount_IdAndJournalEntry_TimestampBetween(accountId, start, end)) {
            if (p.getType() == PostingType.DEBIT) deb = deb.add(p.getAmount());
            else cred = cred.add(p.getAmount());
        }
        return new ReportDtos.BalanceResponse(deb, cred);
    }

    public ReportDtos.TrialBalanceResponse trialBalance(LocalDate from, LocalDate to) {
        LocalDateTime start = from != null ? from.atStartOfDay() : LocalDate.of(1970,1,1).atStartOfDay();
        LocalDateTime end   = to   != null ? to.atTime(LocalTime.MAX) : LocalDate.of(2999,12,31).atTime(LocalTime.MAX);

        Map<Long, BigDecimal> deb = new HashMap<>();
        Map<Long, BigDecimal> cred = new HashMap<>();

        postings.findByJournalEntry_TimestampBetween(start, end).forEach(p -> {
            Long id = p.getAccount().getId();
            if (p.getType() == PostingType.DEBIT) deb.put(id, deb.getOrDefault(id, BigDecimal.ZERO).add(p.getAmount()));
            else cred.put(id, cred.getOrDefault(id, BigDecimal.ZERO).add(p.getAmount()));
        });

        List<ReportDtos.TrialBalanceRow> rows = accounts.findAll().stream().map(a -> {
            BigDecimal d = deb.getOrDefault(a.getId(), BigDecimal.ZERO);
            BigDecimal c = cred.getOrDefault(a.getId(), BigDecimal.ZERO);
            BigDecimal net = d.subtract(c);
            BigDecimal debitCol = net.signum() >= 0 ? net : BigDecimal.ZERO;
            BigDecimal creditCol = net.signum() < 0 ? net.abs() : BigDecimal.ZERO;
            return new ReportDtos.TrialBalanceRow(a.getId(), a.getName(), a.getType(), debitCol, creditCol);
        }).collect(Collectors.toList());

        BigDecimal totalD = rows.stream().map(r -> r.debit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalC = rows.stream().map(r -> r.credit).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ReportDtos.TrialBalanceResponse(rows, totalD, totalC);
    }

    public ReportDtos.IncomeStatementResponse incomeStatement(LocalDate from, LocalDate to) {
        LocalDateTime start = from != null ? from.atStartOfDay() : LocalDate.of(1970,1,1).atStartOfDay();
        LocalDateTime end   = to   != null ? to.atTime(LocalTime.MAX) : LocalDate.of(2999,12,31).atTime(LocalTime.MAX);

        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;

        postings.findByJournalEntry_TimestampBetween(start, end).forEach(p -> {
            AccountType t = p.getAccount().getType();
            if (t == AccountType.INCOME && p.getType() == PostingType.CREDIT) {
                revenue = revenue.add(p.getAmount());
            }
            if (t == AccountType.EXPENSE && p.getType() == PostingType.DEBIT) {
                expenses = expenses.add(p.getAmount());
            }
        });

        return new ReportDtos.IncomeStatementResponse(revenue, expenses);
    }
}
