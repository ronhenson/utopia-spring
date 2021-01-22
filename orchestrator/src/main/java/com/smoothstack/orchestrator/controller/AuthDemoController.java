package com.smoothstack.orchestrator.controller;

import com.smoothstack.orchestrator.security.SecurityUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// Provide a route to demonstrate working JWT Authorization
@RestController
@RequestMapping("/getRole")
public class AuthDemoController {
    
    @GetMapping
    public ResponseEntity<String> getRole(Authentication auth) {
        return ResponseEntity.ok("You are logged in with role: " + SecurityUtils.getRole(auth));
    }
}
