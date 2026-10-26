package com.example.financetracker.expense;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")

public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping
    public Expense saveExpense(@RequestBody Expense expense){
        return expenseService.saveExpense(expense);
    }

    @GetMapping("/{id}")
    public Optional<Expense> getExpenseById(@PathVariable long id){
        return expenseService.getExpenseById(id);
    }

    @GetMapping
    public List<Expense> getAllExpenses(){
        return expenseService.getAllExpenses();
    }

    @DeleteMapping("/{id}")
    public void deleteExpenses(@PathVariable long id){
        expenseService.deleteExpense(id);
    }
}
