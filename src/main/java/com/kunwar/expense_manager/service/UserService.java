package com.kunwar.expense_manager.service;

import com.kunwar.expense_manager.entity.User;
import com.kunwar.expense_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public User createUser(User user){
        return repository.save(user);
    }
    public Optional<User> getById(String id){
        return repository.findById(id);
    }
    public boolean existsById(String id){
        return repository.existsById(id);
    }
}
