package com.kunwar.expense_manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Setter
@Entity
@Table(name = "recurring_expenses")
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(length = 3)
    private String currency = "INR";
    @Column(name = "billing_cycle", nullable = false)
    private String billingCycle; // daily, weekly, monthly, yearly

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "next_billing_date", nullable = false)
    private LocalDate nextBillingDate;

    @Column(length = 20)
    private String status = "active";

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onExpenseCreate(){
        if(this.startDate== null){
            this.startDate= LocalDate.now();
        }
        if(this.nextBillingDate==null && this.billingCycle!=null){
            switch (this.billingCycle.toUpperCase()){
                case "WEEKLY":
                    this.nextBillingDate=startDate.plusWeeks(1);
                    break;

                case "MONTHLY":
                    this.nextBillingDate= startDate.plusMonths(1);
                    break;

                case "YEARLY":
                    this.nextBillingDate=startDate.plusYears(1);
                    break;
                default:
                    this.nextBillingDate=startDate.plusMonths(1);
            }
        }
    }
}
