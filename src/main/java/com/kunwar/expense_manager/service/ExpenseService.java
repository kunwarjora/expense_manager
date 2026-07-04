package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {
    @Autowired
    private RecurringExpenseRepository expenseRepository;

    public List<Expenses> getAllExpenses(String userId){
        return expenseRepository.findByUserIdOrderByNextBillingDateAsc(userId);
    }

    public Expenses createExpense(Expenses expense){
        return expenseRepository.save(expense);
    }

    public Expenses updateExpense(UUID uuid, String userId, Expenses e){
        return expenseRepository.findById(uuid)
                .filter(exp -> exp.getUserId().equals(userId))
                .map(exp ->{
                    exp.setTitle(e.getTitle());
                    exp.setAmount(e.getAmount());
                    exp.setBillingCycle(e.getBillingCycle());
                    exp.setStatus(e.getStatus());
                    exp.setCategory(e.getCategory());
                    return expenseRepository.save(exp);
                }).orElse(null);
    }
    public Boolean delete(UUID uuid, String userId){
        return expenseRepository.findById(uuid)
                .filter(expenses -> expenses.getUserId().equals(userId))
                .map(expenses -> {
                    expenseRepository.delete(expenses);
                    return true;
                }).orElse(false);

    }
    public Double getCurrentMonthTotal(String userId){
        return expenseRepository.sumExpensesForCurrentMonth(userId, LocalDate.now().withDayOfMonth(1), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
    }
    private BigDecimal convertToMonthly(Expenses expense) {
        BigDecimal amount = expense.getAmount();
        switch (expense.getBillingCycle().toLowerCase()) {
            case "daily":
                return amount.multiply(BigDecimal.valueOf(30));
            case "weekly":
                return amount.multiply(BigDecimal.valueOf(52)).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "yearly":
                return amount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "monthly":
            default:
                return amount;
        }
    }

}
