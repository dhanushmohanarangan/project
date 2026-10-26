package com.example.financetracker.report;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseRepository;
import com.example.financetracker.invoice.Invoice;
import com.example.financetracker.invoice.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final ExpenseRepository expenseRepository;
    private final InvoiceRepository invoiceRepository;

    public ReportService(ExpenseRepository expenseRepository, InvoiceRepository invoiceRepository) {
        this.expenseRepository = expenseRepository; this.invoiceRepository = invoiceRepository;
    }
    public long getTotalExpenses(){
        return expenseRepository.findAll().stream().mapToLong(Expense::getAmount).sum();
    }
    public Map<String, Long> getCategoryWiseExpenses(){
        Map<String, Long> report = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            report.put( category, report.getOrDefault(category, 0L) + expense.getAmount() );
        }
        return report;
    }
    public Map<String, Object> getInvoiceSummary() {
        List<Invoice> invoices = invoiceRepository.findAll();
        long totalAmount = 0;
        long paidAmount = 0;
        long pendingAmount = 0;
        for (Invoice invoice : invoices) {
            long amount = invoice.getTotalAmount();
            totalAmount += amount; if ("Paid".equalsIgnoreCase(invoice.getStatus())){
                paidAmount += amount;
            }
            else {
                pendingAmount += amount;
            }
        }
        Map<String, Object> report = new HashMap<>();
        report.put("totalInvoiceAmount", totalAmount);
        report.put("paidAmount", paidAmount);
        report.put("pendingAmount", pendingAmount);
        return report;
    }
}