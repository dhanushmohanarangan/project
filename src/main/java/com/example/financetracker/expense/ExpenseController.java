package com.example.financetracker.expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense saved = service.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return service.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return service.getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/web/update/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> updateExpenseFromForm(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("amount") long amount,
            @RequestParam("category") String category,
            @RequestParam("expenseDate") String expenseDate,
            @RequestParam(value = "description", required = false) String description) {

        Expense updatedDetails = new Expense();
        updatedDetails.setTitle(title);
        updatedDetails.setAmount(amount);
        updatedDetails.setCategory(category);
        updatedDetails.setExpenseDate(LocalDate.parse(expenseDate));
        updatedDetails.setDescription(description);

        service.updateExpense(id, updatedDetails);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/dashboard")
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody Expense expense) {
        return service.updateExpense(id, expense)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (service.deleteExpense(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
