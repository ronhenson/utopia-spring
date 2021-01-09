package com.smoothstack.flights.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights/{origin}/{dest}")
public class FlightController {
    @GetMapping()
    public ResponseEntity<String> getAllFlights(@PathVariable String origin, @PathVariable String dest) {

        return ResponseEntity.status(HttpStatus.OK).body(origin + dest);
    }
}
