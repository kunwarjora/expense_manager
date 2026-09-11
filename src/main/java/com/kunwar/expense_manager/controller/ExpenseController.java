package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.dto.ExpenseRequest;
import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService service){
        this.expenseService = service;
    }


    @GetMapping
    public ResponseEntity<List<Expenses>> getAllExpenses(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(expenseService.getAllExpenses(userId));
    }

    @PostMapping
    public ResponseEntity<Expenses> createExpense(@AuthenticationPrincipal Jwt jwt ,@RequestBody ExpenseRequest expenseRequest) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(expenseService.createExpense(userId, expenseRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenses> updateExpense(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ExpenseRequest expenseRequest) {

        String userId = jwt.getSubject();
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        Expenses expense = expenseService.updateExpense(uuid, userId, expenseRequest);

        if(expense!=null){
            return ResponseEntity.ok(expense);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id, @AuthenticationPrincipal Jwt jwt ) {
        String userId = jwt.getSubject();
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        boolean b = expenseService.delete(uuid, userId);
        if(b){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/current-month-total")
    public ResponseEntity<Double> getCurrentMonthTotal(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        Double total = expenseService.getCurrentMonthTotal(userId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> markAsPaid(@PathVariable String id, @AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseService.pay(uuid, userId));

    }
}
