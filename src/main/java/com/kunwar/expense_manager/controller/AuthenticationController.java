package com.kunwar.expense_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @RequestMapping("/register")
    public ResponseEntity<?> register(){
        return null;
    }
    @RequestMapping("/login")
    public ResponseEntity<?> login(){
        return null;
    }
}
