package com.smoothstack.orchestrator.controller;

import com.smoothstack.orchestrator.entity.AuthResponse;
import com.smoothstack.orchestrator.entity.ConfirmationToken;
import com.smoothstack.orchestrator.entity.User;
import com.smoothstack.orchestrator.exception.EmailNotFoundException;
import com.smoothstack.orchestrator.service.ConfirmationTokenService;
import com.smoothstack.orchestrator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @PostMapping("/sign-up")
    ResponseEntity<Object> signUp(@RequestBody User user) {
        AuthResponse response = new AuthResponse();
        try {
            userService.signUpUser(user);
        } catch (DataIntegrityViolationException e) {
            System.err.println(e);
            response.setMsg("Data integrity violation check JSON syntax");
            response.setDataIntegrityError(true);
            response.setSuccess(false);
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (EmailNotFoundException e) {
            System.err.println(e);
            response.setMsg("User with email " + user.getEmail() + " already exists!");
            response.setEmailIsDuplicate(true);
            response.setSuccess(false);
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setSuccess(true);
        response.setMsg("Account created confirm by email");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/confirm/{token}")
    ResponseEntity<AuthResponse> confirmMail(@PathVariable("token") String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService
                .findConfirmationTokenByToken(token);
        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        if (optionalConfirmationToken.isPresent()) {
            userService.confirmUser(optionalConfirmationToken.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/check-email/{email}")
    ResponseEntity<AuthResponse> checkIfMailExists(@PathVariable("email") String email) {
        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        response.setEmailIsDuplicate(userService.userExists(email));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
