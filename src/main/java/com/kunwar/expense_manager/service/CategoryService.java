package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> getCategories(String userId){
        return repository.findAllByGlobalOrUserId(userId);
    }
    public List<Category> getCategories(){
        return repository.findAllDefault();
    }
    public Category setCustomCategory(Category category){
        return repository.save(category);
    }
}
