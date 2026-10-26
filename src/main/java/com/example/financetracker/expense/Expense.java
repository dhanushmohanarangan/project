package com.example.financetracker.expense;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long expenseId;
    private String title;
    private long amount;
    private String category;
    private LocalDate expenseDate;
    private String Description;

    public Expense(){

    }

    public Expense(long expenseId, String title, long amount, String category, LocalDate expenseDate, String description) {
        this.expenseId = expenseId;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        Description = description;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public String getTitle() {
        return title;
    }

    public long getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
