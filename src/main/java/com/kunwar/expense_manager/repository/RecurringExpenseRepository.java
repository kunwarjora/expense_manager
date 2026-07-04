package com.kunwar.expense_manager.repository;

import com.kunwar.expense_manager.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecurringExpenseRepository extends JpaRepository<Expenses, UUID> {

    List<Expenses> findByUserIdOrderByNextBillingDateAsc(String userId);
    List<Expenses> findByUserId(String userId);
    List<Expenses> findByUserIdAndStatus(String userId, String status);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expenses e where e.userId = :userId AND e.nextBillingDate >= :startOfMonth AND e.nextBillingDate <= :endOfMonth")
    Double sumExpensesForCurrentMonth(
            @Param("userId") String userId,@Param("startOfMonth") LocalDate startOfMonth,@Param("endOfMonth") LocalDate endOfMonth
    );

}
