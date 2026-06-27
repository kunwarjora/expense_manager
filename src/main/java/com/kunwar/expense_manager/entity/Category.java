package com.kunwar.expense_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(name = "color_code")
    private String colorCode;
    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

}
