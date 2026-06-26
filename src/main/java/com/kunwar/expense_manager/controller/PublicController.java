package com.kunwar.expense_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @RequestMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

}
