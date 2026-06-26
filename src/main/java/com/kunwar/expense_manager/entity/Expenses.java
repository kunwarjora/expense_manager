package com.kunwar.expense_manager.entity;

import com.kunwar.expense_manager.enums.Category;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data

public class Expenses {
    @Id
    private long id;
    private long user_id;
    private long category_id;
    private String title;
    private int amount;
    private Category category;
    private Date startDate;
}
