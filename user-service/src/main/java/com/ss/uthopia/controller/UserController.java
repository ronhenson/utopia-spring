package com.ss.uthopia.controller;

import com.ss.uthopia.entity.Booking;
import com.ss.uthopia.entity.User;
import com.ss.uthopia.service.BookingService;
import com.ss.uthopia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private  BookingService bookingService;


    @GetMapping("")
    public ResponseEntity<List<User>> findById(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "username", required = false) String username,
                                               @RequestParam(value = "userId", required = false) Long userId,
                                               @RequestParam(value = "findall", required = false) boolean findAll) {
        List<User> users;
        if(findAll)
            users = userService.findAll();
        else if (userId != null) {
            Optional<User> userOptional = userService.findById(userId);
            users = userOptional.isPresent() ? new ArrayList<>(Arrays.asList(userOptional.get())) : null;
        } else if (username != null && name != null)
            users = userService.findByNameAndUsername(name, username);
        else if (username != null)
            users = userService.findByUsername(username);
        else if (name != null)
            users = userService.findByName(name);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if(users.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable long id) {
        Optional<User> users= userService.findById(id);
        if(!users.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not delete user with id: " + id + "\n" + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("User with id: " + id.toString() + " deleted successfully");
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(users));
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User users) {
        if(userService.userExists(users.getUserId()))
            return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(users));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<Booking> getBookings(@PathVariable long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(bookingService.findById(id).get());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
