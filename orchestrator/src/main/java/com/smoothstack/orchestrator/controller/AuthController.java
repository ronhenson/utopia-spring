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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @PostMapping("/sign-up")
    ResponseEntity<Object> signUp(@RequestBody User user) throws URISyntaxException {
        AuthResponse response = new AuthResponse();
        try {
            User createdUser = userService.signUpUser(user);
            response.setSuccess(true);
            response.setMsg("Account created confirm by email");
            return ResponseEntity.created(new URI("/users/" + createdUser.getUserId())).body(response);
        } catch (DataIntegrityViolationException e) {
            System.err.println(e);
            response.setMsg("Data integrity violation check JSON syntax");
            response.setDataIntegrityError(true);
            response.setSuccess(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (EmailNotFoundException e) {
            System.err.println(e);
            response.setMsg("User with email " + user.getEmail() + " already exists!");
            response.setEmailIsDuplicate(true);
            response.setSuccess(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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

    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie jwtCookieRemove = new Cookie("jwt", "");
        jwtCookieRemove.setMaxAge(0);
        response.addCookie(jwtCookieRemove);
        return ResponseEntity.ok(null);
    }
}
