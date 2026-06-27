package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategories(@RequestHeader("X-User-Id") String userId) {
        return categoryRepository.findAllByGlobalOrUserId(userId);
    }

    @PostMapping
    public Category createCustomCategory(@RequestHeader("X-User-Id") String userId, @RequestBody Category category) {
        category.setUserId(userId); // Ties custom category exclusively to the active caller
        return categoryRepository.save(category);
    }
}
