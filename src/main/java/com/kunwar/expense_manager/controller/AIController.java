package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.service.FinancialAdvisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {
    private final FinancialAdvisorService financialAdvisorService;

    AIController(FinancialAdvisorService financialAdvisorService){
        this.financialAdvisorService = financialAdvisorService;
    }
    @GetMapping("health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok(financialAdvisorService.health());
    }

    @GetMapping("/roast-my-sub")
    public ResponseEntity<String> roastSubscriptions(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        String response=financialAdvisorService.generateRoast(userId);
        if(response==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
