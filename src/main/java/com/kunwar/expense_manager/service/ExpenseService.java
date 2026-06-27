package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExpenseService {
    @Autowired
    private RecurringExpenseRepository expenseRepository;

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
