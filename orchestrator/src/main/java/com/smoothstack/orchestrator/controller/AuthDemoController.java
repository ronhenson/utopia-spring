package com.smoothstack.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// Provide a route to demonstrate working JWT Authorization
@RestController
@RequestMapping("/authenticated")
public class AuthDemoController {
    
    @GetMapping
    public ResponseEntity<String> isAuthenticated() {
        return ResponseEntity.ok("You are logged in!");
    }
}
