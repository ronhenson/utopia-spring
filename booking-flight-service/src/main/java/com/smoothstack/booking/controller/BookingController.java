package com.smoothstack.booking.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.BookingRequest;
import com.smoothstack.booking.exceptions.ResourceExistsException;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;
import com.smoothstack.booking.service.BookingService;

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
		Booking booking = bookingService.getBookingById(bookingId.longValue());
		if (booking.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")
			|| userRole.equals("ADMIN")) {
			return ResponseEntity.ok(booking);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Booking>> getBookings(@RequestHeader("user-id") Integer userId,
			@RequestHeader("user-role") String userRole) {
		if (userRole.equals("EMPLOYEE") || userRole.equals("ADMIN")) {
			return ResponseEntity.ok(bookingService.getAllBookings());
		} else if (userRole.equals("USER")) {
			return ResponseEntity.ok(bookingService.getByUserId(userId));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@PostMapping("/flight/{flightId}")
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request, @PathVariable Long flightId,
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole)
			throws ResourceExistsException, ResourceNotFoundException, URISyntaxException {
			System.out.println("request" + request.getStripeId());
		Booking booking = request.getBooking();
		booking.setBookerId(userId.intValue());
		bookingService.createBooking(booking, flightId, request.getTravelerIds());
		return ResponseEntity.created(new URI("/booking/" + booking.getBookingId())).body(booking);
	}

	@PutMapping
	public ResponseEntity<Booking> updateBooking(
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole,
			@RequestBody Booking booking) throws ResourceNotFoundException {
		Booking booked = bookingService.getBookingById(booking.getBookingId().longValue());
		if (booked.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")) {
			booking.setBookerId(userId.intValue());
			Booking updatedBooking = bookingService.updateBooking(booking);
			return ResponseEntity.ok(updatedBooking);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<Void> deleteBooking(@PathVariable Integer bookingId, 
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole)
			throws ResourceNotFoundException {
		Booking booking = bookingService.getBookingById(bookingId.longValue());
		if (booking.getBookerId().longValue() == userId || userRole.equals("EMPLOYEE")) {
			bookingService.deleteBooking(bookingId);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}