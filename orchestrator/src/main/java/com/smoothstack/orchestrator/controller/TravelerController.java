package com.smoothstack.orchestrator.controller;

import com.smoothstack.orchestrator.entity.Traveler;
import com.smoothstack.orchestrator.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/traveler")
public class TravelerController {

  @Autowired
	RestTemplate restTemplate;

  private final String URL = "http://booking-service/traveler";

  // TODO: require authentication for all routes using this auth parameter, or change auth to not be required by checking for null
  @PutMapping
	public ResponseEntity<Traveler> updateTraveler(@RequestBody Traveler traveler, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		RequestEntity<Traveler> request = RequestEntity.put(URL).header("user-id", auth.getPrincipal().toString())
				.header("user-role", userRole).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON).body(traveler);
		return restTemplate.exchange(request, Traveler.class);
	}
	
	@DeleteMapping("/{travelerId}")
	public ResponseEntity<String> deleteTraveler(@PathVariable Integer travelerId, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		RequestEntity<Void> request = RequestEntity.delete(URL + "/" + travelerId).header("user-id", auth.getPrincipal().toString())
				.header("user-role", userRole).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, String.class);
	}

	@PostMapping("/booking/{bookingId}")
	public ResponseEntity<Traveler> addTravelerToBooking(@RequestBody Traveler traveler, @PathVariable Integer bookingId, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		RequestEntity<Traveler> request = RequestEntity.post(URL + "/booking/" + bookingId).header("user-id", auth.getPrincipal().toString())
				.header("user-role", userRole).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON).body(traveler);
		System.out.println(traveler);
		return restTemplate.exchange(request, Traveler.class);
	}
}
