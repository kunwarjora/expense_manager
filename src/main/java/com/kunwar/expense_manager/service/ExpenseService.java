package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.dto.ExpenseRequest;
import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.entity.Expenses;
import com.kunwar.expense_manager.entity.PaymentHistory;
import com.kunwar.expense_manager.repository.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {
    private final RecurringExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final PaymentHistoryService paymentHistoryService;

    ExpenseService(RecurringExpenseRepository expenseRepository, CategoryService categoryService, PaymentHistoryService paymentHistoryService){
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
        this.paymentHistoryService = paymentHistoryService;
    }

    public List<Expenses> getAllExpenses(String userId){
        return expenseRepository.findByUserIdOrderByNextBillingDateAsc(userId);
    }
    public Expenses findById(UUID id){
        return expenseRepository.findById(id).orElse(null);
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


//When adding expense in the past, next billing is only setting for next month/year, and not the current month/year.

//    UPDATED
    public Expenses createExpense(String userId, ExpenseRequest expenseRequest) {
        Expenses expense = new Expenses();
        expense.setUserId(userId);
        expense.setAmount(expenseRequest.getAmount());
        expense.setTitle(expenseRequest.getTitle());
        expense.setBillingCycle(expenseRequest.getBillingCycle());
        expense.setStartDate(expenseRequest.getStartDate());

        Category category ;
        Optional<Category> category_opt = categoryService.getCategory(expenseRequest.getCategoryId());
        category_opt.ifPresent(expense::setCategory);
        expenseRepository.save(expense);
        return expense;
    }

//    When Updating, status field is getting null. and whole data needs to be passed.
    public Expenses updateExpense(UUID uuid, String userId, ExpenseRequest e) {
        return expenseRepository.findById(uuid)
                .filter(exp -> exp.getUserId().equals(userId))
                .map(exp -> {
                    exp.setTitle(e.getTitle());
                    exp.setAmount(e.getAmount());
                    exp.setBillingCycle(e.getBillingCycle());
                    exp.setStatus(e.getStatus());
                    Optional<Category> category_opt = categoryService.getCategory(e.getCategoryId());
                    if(category_opt.isPresent()){
                        exp.setCategory(category_opt.get());
                    }
//                    For now, we cannot change the startDate
//                    exp.setStartDate(e.getStartDate());
                    return expenseRepository.save(exp);
                }).orElse(null);
    }

    public String pay(UUID uuid, String userId) {
        Expenses expenses = findById(uuid);
        if( expenses==null || !expenses.getUserId().equals(userId)){
            return "Expense Not found";
        }else{
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
                expenseRepository.save(expenses);
            }
            return "Paid";
        }
    }

}
