package com.kunwar.expense_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunwar.expense_manager.dto.KindeWebhookEvent;
import com.kunwar.expense_manager.entity.User;
import com.kunwar.expense_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("api/v1/webhooks")
public class WebhookController {

    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @PostMapping(value = "/kinde", consumes = {"application/jwt", "application/jwt;charset=UTF-8"})
    public ResponseEntity<String> handleKindeWebhook(@RequestBody String jwtToken){
        System.out.println("WEBHOOK RECEIVED!");
        try{
        String[] chunks = jwtToken.split("\\.");
        if(chunks.length<2){
            return ResponseEntity.badRequest().body("Invalid Jwt Token");
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payloadJson = new String(decoder.decode(chunks[1]));
        System.out.println("📦 RAW JSON: " + payloadJson);
        KindeWebhookEvent event = objectMapper.readValue(payloadJson, KindeWebhookEvent.class);
            System.out.println("🎯 EVENT TYPE FOUND: " + event.getType());
        if(event.getType().equals("user.created")){
            String kindeId = event.getData().getUser().getId();
            String email = event.getData().getUser().getEmail();
            System.out.println("👤 EXTRACTED USER: " + kindeId + " | " + email);
            if(!userService.existsById(kindeId)){
                User newUser = new User();
                newUser.setId(kindeId);
                newUser.setEmail(email);
                userService.createUser(newUser);
                System.out.println("New user synced from Kinde: " + email);
            }
        }else{
            System.out.println("IGNORING: Event type is not 'user.created'");
        }

        return ResponseEntity.ok("Webhook processed");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to process webhook");
        }
    }
}
