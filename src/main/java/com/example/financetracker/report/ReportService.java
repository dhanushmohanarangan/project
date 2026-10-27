package com.example.financetracker.report;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final ExpenseService expenseService;

    public ReportService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public Map<String, Long> getTotalExpenses() {

        long total = 0;

        for (Expense expense : expenseService.getAllExpenses()) {
            total += expense.getAmount();
        }

        Map<String, Long> result = new HashMap<>();
        result.put("totalExpenses", total);

        return result;
    }

    public Map<String, Long> getCategoryWiseExpenses() {

        Map<String, Long> result = new HashMap<>();

        for (Expense expense : expenseService.getAllExpenses()) {

            String category = expense.getCategory();

            result.put(
                    category,
                    result.getOrDefault(category, 0L)
                            + expense.getAmount()
            );
        }

        return result;
    }
}