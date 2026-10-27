package com.example.financetracker.report;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/total")
    public Map<String, Long> getTotalExpenses() {
        return reportService.getTotalExpenses();
    }

    @GetMapping("/category-wise")
    public Map<String, Long> getCategoryWiseExpenses() {
        return reportService.getCategoryWiseExpenses();
    }
}