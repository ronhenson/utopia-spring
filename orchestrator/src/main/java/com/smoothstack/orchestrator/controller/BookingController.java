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
		RequestEntity<Void> request = RequestEntity.get(URL + "/" + bookingId).header("user-id", auth.getPrincipal().toString())
				.accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Booking.class);
	}

	@GetMapping
	public ResponseEntity<Booking[]> getAllBookings(Authentication auth) {
		System.out.println("role " + SecurityUtils.getRole(auth));
		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, Booking[].class);
	}

	@PostMapping("/flight/{flightId}")
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest body, @PathVariable Integer flightId) {
		RequestEntity<BookingRequest> request = RequestEntity.post("%s/flight/%d".formatted(URL, flightId))
				.accept(MediaType.APPLICATION_JSON).body(body);
		return restTemplate.exchange(request, Booking.class);
	}

	@PutMapping
	public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking) {
		RequestEntity<Booking> request = RequestEntity.put(URL).accept(MediaType.APPLICATION_JSON).body(booking);
		return restTemplate.exchange(request, Booking.class);
	}
	
	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId) {
		RequestEntity<Void> request = RequestEntity.delete("%s/%d".formatted(URL, bookingId)).accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.exchange(request, String.class);
	}
}
