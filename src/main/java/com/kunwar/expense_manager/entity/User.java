package com.kunwar.expense_manager.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class User {
    @Id
    private Long id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String email;

}
