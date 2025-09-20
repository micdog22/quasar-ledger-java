
package com.example.quasarledger.web.dto;

import com.example.quasarledger.domain.AccountType;
import java.math.BigDecimal;
import java.util.List;

public class ReportDtos {

    public static class BalanceResponse {
        public BigDecimal debits;
        public BigDecimal credits;
        public BigDecimal net; // debits - credits
        public BalanceResponse(BigDecimal d, BigDecimal c) {
            this.debits = d; this.credits = c; this.net = d.subtract(c);
        }
    }

    public static class TrialBalanceRow {
        public Long accountId;
        public String accountName;
        public AccountType type;
        public BigDecimal debit;
        public BigDecimal credit;

        public TrialBalanceRow(Long accountId, String accountName, AccountType type, BigDecimal debit, BigDecimal credit) {
            this.accountId = accountId;
            this.accountName = accountName;
            this.type = type;
            this.debit = debit;
            this.credit = credit;
        }
    }

    public static class TrialBalanceResponse {
        public List<TrialBalanceRow> rows;
        public BigDecimal totalDebit;
        public BigDecimal totalCredit;
        public TrialBalanceResponse(List<TrialBalanceRow> rows, BigDecimal totalDebit, BigDecimal totalCredit) {
            this.rows = rows; this.totalDebit = totalDebit; this.totalCredit = totalCredit;
        }
    }

    public static class IncomeStatementResponse {
        public BigDecimal revenue;    // credits in INCOME accounts
        public BigDecimal expenses;   // debits in EXPENSE accounts
        public BigDecimal result;     // revenue - expenses
        public IncomeStatementResponse(BigDecimal revenue, BigDecimal expenses) {
            this.revenue = revenue; this.expenses = expenses; this.result = revenue.subtract(expenses);
        }
    }
}
