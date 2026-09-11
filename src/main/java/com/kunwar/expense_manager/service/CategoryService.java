package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.Category;
import com.kunwar.expense_manager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    CategoryService(CategoryRepository repository){
        this.repository=repository;
    }

    public Optional<Category> getCategory(int id){
        return repository.findById(id);
    }

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
