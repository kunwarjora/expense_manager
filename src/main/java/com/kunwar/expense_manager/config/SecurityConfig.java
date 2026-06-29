package com.kunwar.expense_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request-> request
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/api/v1/health").permitAll()
                .anyRequest().authenticated()
        )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()))
                .build();

    }

    @Bean
    @Order(1)
    public SecurityFilterChain webhookSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher("/api/v1/webhooks/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> auth.anyRequest().permitAll())
                .build();
    }
}
