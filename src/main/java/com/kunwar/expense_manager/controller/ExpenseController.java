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

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<Expenses> getAllExpenses(@RequestHeader("X-User-Id") String userId) {
        return expenseRepository.findByUserId(userId);
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
        return expenseRepository.save(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenses> updateExpense(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId,
            @RequestBody ExpenseRequest updatedDetails) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        Category category = new Category();
                category.setId(updatedDetails.getCategoryId());

        return expenseRepository.findById(uuid)
                .filter(exp -> exp.getUserId().equals(userId))
                .map(exp -> {
                    exp.setTitle(updatedDetails.getTitle());
                    exp.setAmount(updatedDetails.getAmount());
                    exp.setBillingCycle(updatedDetails.getBillingCycle());
                    exp.setStatus(updatedDetails.getStatus());
                    exp.setCategory(category);
                    return ResponseEntity.ok(expenseRepository.save(exp));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id, @RequestHeader("X-User-Id") String userId) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return expenseRepository.findById(uuid)
                .filter(exp -> exp.getUserId().equals(userId))
                .map(exp -> {
                    expenseRepository.delete(exp);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
