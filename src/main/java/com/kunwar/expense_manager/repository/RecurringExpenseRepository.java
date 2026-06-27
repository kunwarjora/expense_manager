package com.kunwar.expense_manager.repository;

import com.kunwar.expense_manager.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecurringExpenseRepository extends JpaRepository<Expenses, UUID> {
    List<Expenses> findByUserId(String userId);
    List<Expenses> findByUserIdAndStatus(String userId, String status);
}
