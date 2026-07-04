package com.kunwar.expense_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment_history")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "expense_id")
    private String expenseId;
    @Column(name = "user_id")
    private String userId;
    private Double amount;
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @PrePersist
    protected void onCreate(){
        this.paymentDate=LocalDate.now();
    }
}
