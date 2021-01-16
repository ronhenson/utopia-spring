package com.smoothstack.flights.controller;

import com.smoothstack.flights.entity.Flight;
import com.smoothstack.flights.entity.MultiHopFlight;
import com.smoothstack.flights.service.FlightDetailsService;
import com.smoothstack.flights.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired
    FlightService flightService;
    @Autowired
    FlightDetailsService flightDetailsService;

    @GetMapping("/multihop")
    public ResponseEntity<List<MultiHopFlight>> getMultiHopFlts(@RequestParam(value="origin") String origin, @RequestParam(value="dest") String dest, @RequestParam(name = "date") String date) {
        List<MultiHopFlight> flights = flightService.findByMultiHop(origin, dest, date);
        return ResponseEntity.status(HttpStatus.OK).body(flights);
    }

    @GetMapping("")
    public ResponseEntity<List<Flight>> getNonstopFlights(@RequestParam(value="origin") String origin, @RequestParam(value="dest") String dest, @RequestParam(name = "date") String date) {
        List<Flight> flights = flightService.findFlights(origin, dest, date);
        return ResponseEntity.status(HttpStatus.OK).body(flights);
    }
}
