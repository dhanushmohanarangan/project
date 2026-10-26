package com.example.financetracker.web;
import com.example.financetracker.expense.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ExpenseRepository expenseRepository;

    public ViewController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("expenses", expenseRepository.findAll());
        return "dashboard";
    }
}
