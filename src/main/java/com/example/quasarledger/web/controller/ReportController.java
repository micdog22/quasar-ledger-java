
package com.example.quasarledger.web.controller;

import com.example.quasarledger.service.ReportService;
import com.example.quasarledger.web.dto.ReportDtos;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reports;

    public ReportController(ReportService reports) {
        this.reports = reports;
    }

    @GetMapping("/trial-balance")
    public ReportDtos.TrialBalanceResponse trialBalance(@RequestParam(required = false) LocalDate from,
                                                        @RequestParam(required = false) LocalDate to) {
        return reports.trialBalance(from, to);
    }

    @GetMapping("/income-statement")
    public ReportDtos.IncomeStatementResponse income(@RequestParam(required = false) LocalDate from,
                                                     @RequestParam(required = false) LocalDate to) {
        return reports.incomeStatement(from, to);
    }
}
