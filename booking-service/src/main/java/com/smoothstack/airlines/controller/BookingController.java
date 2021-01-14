package com.smoothstack.airlines.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

	@GetMapping("/{bookingId}")
	public ResponseEntity<Booking> getBookingById(@PathVariable Integer bookingId,
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole) 
			throws ResourceNotFoundException {
		System.out.println("role " + userRole);
		Booking booking = bookingService.getBookingById(bookingId.longValue());
		if (booking.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")) {
			return ResponseEntity.ok(booking);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Booking>> getBookings(@RequestHeader("user-id") Integer userId,
			@RequestHeader("user-role") String userRole) {
			System.out.println("inside getBookings role " + userRole);
		if (userRole.equals("EMPLOYEE")) {
			return ResponseEntity.ok(bookingService.getAllBookings());
		} else if (userRole.equals("USER")) {
			return ResponseEntity.ok(bookingService.getByUserId(userId));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}


	@PostMapping("/flight/{flightId}")
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request, @PathVariable Long flightId,
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole)
			throws ResourceExistsException, ResourceNotFoundException, URISyntaxException {
		Booking booking = request.getBooking();
		System.out.println("Inside Post Airlines booking " + booking);

		bookingService.createBooking(booking, flightId, request.getTravelerIds());
		return ResponseEntity.created(new URI("/booking/" + booking.getBookingId())).body(booking);
	}

	@PutMapping
	public ResponseEntity<Booking> updateBooking(
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole,
			@RequestBody Booking booking) throws ResourceNotFoundException {
		Booking booked = bookingService.getBookingById(booking.getBookingId().longValue());

		System.out.println("In booking airline controller " + booking.getBookerId().longValue() + "  userId " + userId + " booked.bookerId " + booked);

		if (booked.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")) {
			bookingService.updateBooking(booking);
			return ResponseEntity.ok(booking);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId, 
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole)
			throws ResourceNotFoundException {
		Booking booking = bookingService.getBookingById(bookingId.longValue());
		if (booking.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")) {
			bookingService.deleteBooking(bookingId);
			return ResponseEntity.ok("Booking deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}