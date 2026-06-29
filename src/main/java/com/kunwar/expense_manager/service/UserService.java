package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.User;
import com.kunwar.expense_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User createUser(User user){
        return repository.save(user);
    }
    public User getById(String id){
        return repository.getById(id);
    }
    public boolean existsById(String id){
        return repository.existsById(id);
    }
}
