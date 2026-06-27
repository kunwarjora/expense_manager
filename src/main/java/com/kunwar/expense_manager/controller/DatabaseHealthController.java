package com.kunwar.expense_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseHealthController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/v1/health")
    public String checkDatabaseConnection() {
        try {
            // Executes a fast structural test query on Supabase
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (result != null && result == 1) {
                return "🚀 Connection Successful! Spring Boot is talking to Supabase.";
            } else {
                return "⚠️ Database responded, but returned an unexpected payload.";
            }
        } catch (Exception e) {
            return "❌ Connection Failed! Error details: " + e.getMessage();
        }
    }

    @GetMapping("/api/v1/user/me")
    public String getSecureUserInfo(@AuthenticationPrincipal Jwt jwt) {
        // Extract claims provided inside Kinde's signed token
        String kindeUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        return "🔐 Authenticated successfully via Kinde!\n" +
                "Your Unique Kinde ID: " + kindeUserId + "\n" +
                "Your Registered Email: " + email;
    }
}
