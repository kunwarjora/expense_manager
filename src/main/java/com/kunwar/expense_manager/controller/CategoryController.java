package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.repository.CategoryRepository;
import com.kunwar.expense_manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public List<Category> getCategories(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        if(userId!=null){
            return service.getCategories(userId);
        }
        else return service.getCategories();
    }


    @PostMapping
    public Category createCustomCategory(@AuthenticationPrincipal Jwt jwt, @RequestBody Category category) {
        System.out.println("Works till here");
        String userId = jwt.getSubject();
        System.out.println("Works till here as well");
        category.setUserId(userId); // Ties custom category exclusively to the active caller
        return service.setCustomCategory(category);
    }
}
