package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseHealthController {

    private final JdbcTemplate jdbcTemplate;
    DatabaseHealthController( JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @GetMapping("/api/v1/health")
    public ResponseEntity<String> checkDatabaseConnection() {
        try {
            // Executes a fast structural test query on Supabase
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (result != null && result == 1) {
                return ResponseEntity.ok("Connection Successful! Spring Boot is talking to Supabase.");
            } else {
                return ResponseEntity.ok("Database responded, but returned an unexpected payload.");
            }
        } catch (Exception e) {
            return ResponseEntity.ofNullable("Connection Failed! Error details: " + e.getMessage());
        }
    }
    
}
