package com.smoothstack.orchestrator.controller;

import com.smoothstack.orchestrator.entity.ConfirmationToken;
import com.smoothstack.orchestrator.entity.User;
import com.smoothstack.orchestrator.service.ConfirmationTokenService;
import com.smoothstack.orchestrator.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body) throws Exception {
        if (!body.containsKey("email") || !body.containsKey("password")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed login JSON");
        }
        userService.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok("Signed in with email `%s`".formatted(body.get("email")));
    }

    @PostMapping("/sign-up")
    ResponseEntity<Object> signUp(@RequestBody User user) {
        try {
            userService.signUpUser(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Already Exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/confirm")
    String confirmMail(@RequestParam("token") String token) {
        System.out.println(token);
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService
                .findConfirmationTokenByToken(token);
        optionalConfirmationToken.ifPresent(userService::confirmUser);
        return token;
    }

}
