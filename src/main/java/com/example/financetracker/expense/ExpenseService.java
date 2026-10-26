package com.example.financetracker.expense;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService{

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public Expense saveExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Optional<Expense> getExpenseById(long expenseId){
        return expenseRepository.findById(expenseId);
    }

    public void deleteExpense(long expenseId){
         expenseRepository.deleteById(expenseId);
    }
}
