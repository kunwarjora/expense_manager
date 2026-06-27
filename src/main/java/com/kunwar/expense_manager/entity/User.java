package com.kunwar.expense_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    @Column(name = "created_at")
    private Date createdAt;
}
