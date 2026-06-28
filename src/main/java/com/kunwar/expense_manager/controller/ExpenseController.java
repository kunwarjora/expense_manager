package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.dta.ExpenseRequest;
import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import com.kunwar.expense_manager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    @Autowired
    private RecurringExpenseRepository expenseRepository;


    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService service){
        this.expenseService = service;
    }

    @GetMapping
    public List<Expenses> getAllExpenses(@RequestHeader("X-User-Id") String userId) {
        return expenseService.getAllExpenses(userId);
    }

    @PostMapping
    public Expenses createExpense(@RequestHeader("X-User-Id") String userId, @RequestBody ExpenseRequest expenseRequest) {
        Expenses expense = new Expenses();
        expense.setUserId(userId);
        expense.setAmount(expenseRequest.getAmount());
        expense.setTitle(expenseRequest.getTitle());
        expense.setBillingCycle(expenseRequest.getBillingCycle());
        expense.setStartDate(expenseRequest.getStartDate());
        expense.setNextBillingDate(expenseRequest.getStartDate());

        Category category = new Category();
        category.setId(expenseRequest.getCategoryId());
        expense.setCategory(category);
        return expenseService.createExpense(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenses> updateExpense(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId,
            @RequestBody ExpenseRequest expenseRequest) {

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        Expenses expense = new Expenses();
        expense.setUserId(userId);
        expense.setAmount(expenseRequest.getAmount());
        expense.setTitle(expenseRequest.getTitle());
        expense.setBillingCycle(expenseRequest.getBillingCycle());
        expense.setStartDate(expenseRequest.getStartDate());
        expense.setNextBillingDate(expenseRequest.getStartDate());

        Category category = new Category();
        category.setId(expenseRequest.getCategoryId());
        expense.setCategory(category);

        expense  = expenseService.updateExpense(uuid, userId, expense);
        if(expense!=null){
            return ResponseEntity.ok(expense);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id, @RequestHeader("X-User-Id") String userId) {
//String to UUID
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        Boolean b = expenseService.delete(uuid, userId);
        if(b){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }
}
