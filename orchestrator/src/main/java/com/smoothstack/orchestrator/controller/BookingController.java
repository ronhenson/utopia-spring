package com.smoothstack.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smoothstack.orchestrator.entity.Booking;
import com.smoothstack.orchestrator.entity.BookingRequest;
import com.smoothstack.orchestrator.security.SecurityUtils;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	RestTemplate restTemplate;

	private final String URL = "http://booking-service/booking";
	
	@GetMapping("/{bookingId}")
	public ResponseEntity<Booking> getBookingById(@PathVariable Integer bookingId, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		System.out.println("userRole " + userRole + " SecurityUtils " + SecurityUtils.getRole(auth) + " auth.getP " + auth.getPrincipal());
		RequestEntity<Void> request = RequestEntity.get(URL + "/" + bookingId).header("user-id", auth.getPrincipal().toString()).header( "user-role", userRole)
				.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Booking.class);
	}

	@GetMapping
	public ResponseEntity<Booking[]> getAllBookings(Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		System.out.println("userRole " + userRole + " SecurityUtils " + SecurityUtils.getRole(auth) + " auth.getP " + auth.getPrincipal());

		RequestEntity<Void> request = RequestEntity.get(URL).header("user-id", auth.getPrincipal().toString())
			.header( "user-role", userRole).accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Booking[].class);
	}

	@PostMapping("/flight/{flightId}")
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest body, @PathVariable Long flightId, Authentication auth) {
		System.out.println("Hello");
		String userRole = SecurityUtils.getRole(auth);
		// System.out.println("inside Post flightId " + flightId + " user-id " + auth.getPrincipal() + " userRole " + userRole);
		RequestEntity<BookingRequest> request = RequestEntity.post("%s/flight/%d".formatted(URL, flightId))
				.header("user-id", auth.getPrincipal().toString()).header( "user-role", userRole)
				.accept(MediaType.APPLICATION_JSON).body(body);
				System.out.println("Helloagain");

		return restTemplate.exchange(request, Booking.class);
	}

	@PutMapping
	public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		System.out.println("booking " + booking.getBookerId());
		RequestEntity<Booking> request = RequestEntity.put(URL).header("user-id", auth.getPrincipal().toString()).header( "user-role", userRole)
			.accept(MediaType.APPLICATION_JSON).body(booking);
		return restTemplate.exchange(request, Booking.class);
	}
	
	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId, Authentication auth) {
		String userRole = SecurityUtils.getRole(auth);
		RequestEntity<Void> request = RequestEntity.delete("%s/%d".formatted(URL, bookingId))
			.header("user-id", auth.getPrincipal().toString()).header( "user-role", userRole)
			.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, String.class);
	}
}
