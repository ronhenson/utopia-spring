package com.smoothstack.orchestrator.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smoothstack.orchestrator.entity.Airport;

@RestController
@RequestMapping("/airport")
public class AirportController {

	@Autowired
	RestTemplate restTemplate;

	private final String URL = "http://airport-service/airport";

	@GetMapping("/{airportId}")
	public ResponseEntity<Airport> getAirportById(@PathVariable String airportId) {
		RequestEntity<Void> request = RequestEntity.get(URL + "/" + airportId).accept(MediaType.APPLICATION_JSON)
				.build();
		return restTemplate.exchange(request, Airport.class);
	}

	@GetMapping
	public ResponseEntity<Airport[]> findByCity(@RequestParam(value = "city", defaultValue = "") String city) {
		RequestEntity<Void> request = RequestEntity.get("%s?city=%s".formatted(URL, city))
				.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Airport[].class);
	}

	@PostMapping
	public ResponseEntity<Airport> addAirport(@RequestBody Airport airport) {
		RequestEntity<Airport> request = RequestEntity.post(URL).accept(MediaType.APPLICATION_JSON).body(airport);
		return restTemplate.exchange(request, Airport.class);
	}

	@PutMapping
	public ResponseEntity<Airport> updateAirport(@RequestBody Airport airport) {
		RequestEntity<Airport> request = RequestEntity.put(URL).accept(MediaType.APPLICATION_JSON).body(airport);
		return restTemplate.exchange(request, Airport.class);
	}

	@DeleteMapping("/{airportId}")
	public ResponseEntity<Void> deleteAirport(@PathVariable String airportId) {
		RequestEntity<Void> request = RequestEntity.delete(URL + "/" + airportId).accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Void.class);
	}
}
