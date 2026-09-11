package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.PaymentHistory;
import com.kunwar.expense_manager.repository.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentHistoryService {

    private final PaymentHistoryRepository repository;
    PaymentHistoryService(PaymentHistoryRepository repository){
        this.repository = repository;
    }

    public List<PaymentHistory> findByUserId(String userId){
        return repository.findByUserIdOrderByPaymentDateDesc(userId);
    }
    public PaymentHistory saveHistory(PaymentHistory history){
        return repository.save(history);
    }
}
