package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.dto.ExpenseRequest;
import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.entity.PaymentHistory;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import com.kunwar.expense_manager.service.CategoryService;
import com.kunwar.expense_manager.service.ExpenseService;
import com.kunwar.expense_manager.service.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    @Autowired
    private PaymentHistoryService paymentHistoryService;
    @Autowired
    private CategoryService categoryService;


    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService service){
        this.expenseService = service;
    }

    @GetMapping
    public List<Expenses> getAllExpenses(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return expenseService.getAllExpenses(userId);
    }

    @PostMapping
    public Expenses createExpense(@AuthenticationPrincipal Jwt jwt ,@RequestBody ExpenseRequest expenseRequest) {
        String userId = jwt.getSubject();
        Expenses expense = new Expenses();
        expense.setUserId(userId);
        expense.setAmount(expenseRequest.getAmount());
        expense.setTitle(expenseRequest.getTitle());
        expense.setBillingCycle(expenseRequest.getBillingCycle());
        expense.setStartDate(expenseRequest.getStartDate());
//        expense.setNextBillingDate(expenseRequest.getStartDate());

        Category category ;
//        category.setId(expenseRequest.getCategoryId());
        category = categoryService.getCategory(expenseRequest.getCategoryId());
        expense.setCategory(category);
        return expenseService.createExpense(expense);
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

        Expenses expense = new Expenses();
        expense.setUserId(userId);
        expense.setAmount(expenseRequest.getAmount());
        expense.setTitle(expenseRequest.getTitle());
        expense.setBillingCycle(expenseRequest.getBillingCycle());
        expense.setStartDate(expenseRequest.getStartDate());
        expense.setNextBillingDate(expenseRequest.getStartDate());

        Category category;
//        category.setId(expenseRequest.getCategoryId());
        category = categoryService.getCategory(expenseRequest.getCategoryId());
        expense.setCategory(category);

        expense  = expenseService.updateExpense(uuid, userId, expense);
        if(expense!=null){
            return ResponseEntity.ok(expense);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id, @AuthenticationPrincipal Jwt jwt ) {
        String userId = jwt.getSubject();
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

    @GetMapping("/current-month-total")
    public ResponseEntity<Double> getCurrentMonthTotal(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        Double total = expenseService.getCurrentMonthTotal(userId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> markAsPaid(@PathVariable String id, @AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        Expenses expenses = expenseService.findById(uuid);
        if( expenses==null || !expenses.getUserId().equals(userId)){
            return ResponseEntity.status(403).body("Expense Not found");


        }else {
            PaymentHistory history = new PaymentHistory();
            history.setExpenseId(expenses.getId().toString());
            history.setUserId(expenses.getUserId());
            history.setAmount(expenses.getAmount().toBigInteger().doubleValue());
            paymentHistoryService.saveHistory(history);

            if (expenses.getNextBillingDate() != null && expenses.getBillingCycle() != null) {
                switch (expenses.getBillingCycle().toUpperCase()) {
                    case "WEEKLY":
                        expenses.setNextBillingDate(expenses.getNextBillingDate().plusWeeks(1));
                        break;
                    case "MONTHLY":
                        expenses.setNextBillingDate(expenses.getNextBillingDate().plusMonths(1));
                        break;
                    case "YEARLY":
                        expenses.setNextBillingDate(expenses.getNextBillingDate().plusYears(1));
                        break;
                    default:
                        expenses.setNextBillingDate(expenses.getNextBillingDate().plusMonths(1));
                }
                expenseService.updateExpense(expenses.getId(), userId, expenses);
                return ResponseEntity.ok(expenses);
            }
        }
        return null;
    }
}
