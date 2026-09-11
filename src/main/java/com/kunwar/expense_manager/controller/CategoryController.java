package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService service;

    CategoryController(CategoryService service){
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getCategories(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
//        Give User Specific
        if(userId!=null){
            return ResponseEntity.ok(service.getCategories(userId));
        }
//        Give Default Categories
        else return ResponseEntity.ok(service.getCategories());
    }


    @PostMapping
    public ResponseEntity<Category> createCustomCategory(@AuthenticationPrincipal Jwt jwt, @RequestBody Category category) {
        String userId = jwt.getSubject();
        if(userId!=null) {
            category.setUserId(userId); // Ties custom category exclusively to the active caller
            return ResponseEntity.ok(service.setCustomCategory(category));
        }
        return ResponseEntity.badRequest().build();
    }
}
