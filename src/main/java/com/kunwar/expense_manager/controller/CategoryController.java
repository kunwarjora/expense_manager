package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.repository.CategoryRepository;
import com.kunwar.expense_manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public List<Category> getCategories(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        if(userId!=null){
            return service.getCategories(userId);
        }
        else return service.getCategories();
    }


    @PostMapping
    public Category createCustomCategory(@RequestHeader("X-User-Id") String userId, @RequestBody Category category) {
        category.setUserId(userId); // Ties custom category exclusively to the active caller
        return service.setCustomCategory(category);
    }
}
