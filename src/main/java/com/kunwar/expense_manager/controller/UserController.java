package com.kunwar.expense_manager.controller;

import com.kunwar.expense_manager.entity.User;
import com.kunwar.expense_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("api/v1/user/info")
    ResponseEntity<User> getUser(@AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getSubject();
        Optional<User> user = userService.getById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
