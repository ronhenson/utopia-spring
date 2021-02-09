package com.smoothstack.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.smoothstack.orchestrator.entity.Flight;
import com.smoothstack.orchestrator.entity.MultiHopFlight;


@RestController
@RequestMapping("/flights")
public class FlightController {

    private final String URL = "http://booking-service/flights";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/multihop")
    // TODO: change request params to a HashMap
    //    allows us to change the request params in the flight-service without having to modify the params in the orchestrator
    public ResponseEntity<MultiHopFlight[]> getMultiHopFlts(@RequestParam(value="origin") String origin, @RequestParam(value="dest") String dest, @RequestParam(name = "date") String date) {
        RequestEntity<Void> request = RequestEntity.get(String.format("%s/multihop?origin=%s&dest=%s&date=%s", URL, origin, dest, date))
                .accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(request, MultiHopFlight[].class);
    }

    @GetMapping("")
    public ResponseEntity<Flight[]> getNonstopFlights(@RequestParam(value="origin") String origin, @RequestParam(value="dest") String dest, @RequestParam(name = "date") String date) {
        RequestEntity<Void> request = RequestEntity.get(String.format("%s?origin=%s&dest=%s&date=%s", URL, origin, dest, date))
                .accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(request, Flight[].class);
    }

}
