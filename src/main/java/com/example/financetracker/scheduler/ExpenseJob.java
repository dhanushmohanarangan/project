package com.example.financetracker.scheduler;

import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ExpenseJob implements Job {

    private final ExpenseRepository repo;

    public ExpenseJob(ExpenseRepository repo) {
        this.repo = repo;
    }

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("Quartz firing: Adding 5 records...");

        for (int i = 1; i <= 5; i++) {
            Expense exp = new Expense();
            exp.setTitle("Scheduled Expense " + i);
            exp.setAmount(500);
            exp.setCategory("API");
            exp.setExpenseDate(LocalDate.now());
            exp.setDescription("Quartz auto entry");

            repo.save(exp);
        }
    }
}
