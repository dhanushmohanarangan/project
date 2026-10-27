package com.example.financetracker.expense;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense addExpense(Expense expense) {
        return repository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public Optional<Expense> getExpenseById(Long id) {
        return repository.findById(id);
    }

    public Optional<Expense> updateExpense(Long id, Expense input) {
        Optional<Expense> existingData = repository.findById(id);

        if (existingData.isPresent()) {
            Expense existing = existingData.get();
            existing.setTitle(input.getTitle());
            existing.setAmount(input.getAmount());
            existing.setCategory(input.getCategory());
            existing.setExpenseDate(input.getExpenseDate());
            existing.setDescription(input.getDescription());
            return Optional.of(repository.save(existing));
        }

        return Optional.empty();
    }

    public boolean deleteExpense(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Expense> getExpensesByDate(LocalDate date) {
        return repository.findByExpenseDate(date);
    }

    public List<Expense> saveAll(List<Expense> expenses) {
        return repository.saveAll(expenses);
    }
}
