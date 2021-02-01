package com.smoothstack.utopia.controller;

import com.smoothstack.utopia.entity.User;
import com.smoothstack.utopia.entity.UserRole;
import com.smoothstack.utopia.exception.DuplicateEmailException;
import com.smoothstack.utopia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> adminUserSearch(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "findall", required = false) boolean findAll) {
        List<User> users;
        if (findAll)
            users = userService.findAll();
        else if (userId != null) {
            Optional<User> userOptional = userService.findById(userId);
            users = userOptional.isPresent() ? new ArrayList<>(Arrays.asList(userOptional.get())) : null;
        } else if (email != null) {
            Optional<User> userOptional = userService.findByEmail(email);
            users = userOptional.isPresent() ? new ArrayList<>(Arrays.asList(userOptional.get())) : null;
        } else if (firstName != null && lastName != null)
            users = userService.findByFirstNameAndLastName(firstName, lastName);
        else if(firstName != null)
            users = userService.findByFirstName(firstName);
        else if(lastName != null)
            users = userService.findByLastName(lastName);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (users == null || users.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(
            @PathVariable long id,
            @RequestHeader("user-id") Long userId,
            @RequestHeader("user-role") String userRole) {
        if(!userRole.equals(UserRole.ADMIN.name()) && !userId.equals(id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        Optional<User> users = userService.findById(id);
        if (!users.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestHeader("user-id") Long userId,
            @RequestHeader("user-role") String userRole) {
        if(!userRole.equals(UserRole.ADMIN.name()) && !userId.equals(id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        try {
            userService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Could not delete user with id: " + id + "\n" + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("User with id: " + id.toString() + " deleted successfully");
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(
            @RequestBody User user,
            @RequestHeader("user-id") Long userId,
            @RequestHeader("user-role") String userRole) {
        if(!userRole.equals(UserRole.ADMIN.name()) && !userId.equals(user.getUserId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        if (userService.userExists(user.getEmail()))
            return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(user));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
        } catch (DuplicateEmailException | DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

}
