package com.example.financetracker.web;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class ViewController {

    private final ExpenseService expenseService;

    public ViewController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {

        model.addAttribute(
                "expenses",
                expenseService.getAllExpenses()
        );

        model.addAttribute("today", LocalDate.now());

        return "dashboard";
    }

    // Add an expense from the dashboard
    @PostMapping("/web/expenses")
    public String addExpense(
            @RequestParam String title,
            @RequestParam long amount,
            @RequestParam String category,
            @RequestParam LocalDate expenseDate,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {

        if (title.isBlank() || category.isBlank() || amount < 0) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Enter a title, a category, and a non-negative amount."
            );

            return "redirect:/dashboard";
        }

        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setExpenseDate(expenseDate);
        expense.setDescription(description);

        expenseService.addExpense(expense);

        redirectAttributes.addFlashAttribute(
                "message",
                "Expense added successfully."
        );

        return "redirect:/dashboard";
    }

    // Update an existing expense
    @PostMapping("/web/expenses/{id}/update")
    public String updateExpense(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam long amount,
            @RequestParam String category,
            @RequestParam LocalDate expenseDate,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {

        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setExpenseDate(expenseDate);
        expense.setDescription(description);

        if (title.isBlank() || category.isBlank() || amount < 0) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Enter a title, a category, and a non-negative amount."
            );

            return "redirect:/dashboard";
        }

        boolean updated = expenseService.updateExpense(id, expense).isPresent();

        redirectAttributes.addFlashAttribute(
                updated ? "message" : "error",
                updated ? "Expense updated successfully."
                        : "Expense not found."
        );

        return "redirect:/dashboard";
    }

    @PostMapping("/web/expenses/{id}/delete")
    public String deleteExpense(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        boolean deleted = expenseService.deleteExpense(id);

        redirectAttributes.addFlashAttribute(
                deleted ? "message" : "error",
                deleted ? "Expense deleted successfully."
                        : "Expense not found."
        );

        return "redirect:/dashboard";
    }
}