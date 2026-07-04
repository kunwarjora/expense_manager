package com.kunwar.expense_manager.repository;

import com.kunwar.expense_manager.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
    List<PaymentHistory> findByUserIdOrderByPaymentDateDesc(String userId);
}
