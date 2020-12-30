package com.smoothstack.airlines.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.BookingRequest;
import com.smoothstack.airlines.exceptions.ResourceExistsException;
import com.smoothstack.airlines.exceptions.ResourceNotFoundException;
import com.smoothstack.airlines.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	BookingService bookingService;

	@GetMapping("/traveler/{travelerId}")
	public List<Booking> getAllBookings(@PathVariable Integer travelerId) throws ResourceNotFoundException {
		return bookingService.getBookingsByTraveler(travelerId);
	}
	
	@GetMapping
	public List<Booking> getBookings() {
		return bookingService.getAllBookings();
	}

	@PostMapping("/flight/{flightId}")
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request, @PathVariable Integer flightId) throws ResourceExistsException, ResourceNotFoundException, URISyntaxException  {
		Booking booking = request.getBooking();
		bookingService.createBooking(booking, flightId, request.getTravelerIds());
		return ResponseEntity.created(new URI("/booking/" + booking.getBookingId())).body(booking);
	}

	@PutMapping
	public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking) throws ResourceNotFoundException {
		bookingService.updateBooking(booking);
		return ResponseEntity.ok(booking);
	}
	
	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId) throws ResourceNotFoundException {
		bookingService.deleteBooking(bookingId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}
}
