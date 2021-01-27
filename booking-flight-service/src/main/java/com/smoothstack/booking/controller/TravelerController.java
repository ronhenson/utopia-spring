package com.smoothstack.booking.controller;

import java.net.URI;
import java.net.URISyntaxException;

import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.Traveler;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;
import com.smoothstack.booking.service.BookingService;
import com.smoothstack.booking.service.TravelerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/traveler")
public class TravelerController {
  @Autowired
  TravelerService travelerService;

  @Autowired
  BookingService bookingService;

  @PutMapping
	public ResponseEntity<Traveler> updateTraveler(
			@RequestHeader("user-id") Long userId,
			@RequestHeader("user-role") String userRole,
      @RequestBody Traveler traveler) throws ResourceNotFoundException {
        
    if (travelerService.userOwnsTraveler(userId.intValue(), traveler.getTravelerId()) == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    travelerService.updateTraveler(traveler);
    return ResponseEntity.ok(traveler);
  }
  
  @DeleteMapping("/{travelerId}")
  public ResponseEntity<Void> deleteTraveler(
    @RequestHeader("user-id") Long userId,
    @RequestHeader("user-role") String userRole,
    @PathVariable Integer travelerId) throws ResourceNotFoundException {
    
    Integer bookingIdOfTraveler = travelerService.userOwnsTraveler(userId.intValue(), travelerId);
    if (bookingIdOfTraveler == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    travelerService.deleteTraveler(travelerId, bookingIdOfTraveler);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/booking/{bookingId}")
  public ResponseEntity<Traveler> addTravelerToBooking(
    @RequestHeader("user-id") Long userId,
    @RequestHeader("user-role") String userRole,
    @PathVariable Integer bookingId,
    @RequestBody Traveler traveler) throws ResourceNotFoundException, URISyntaxException {

    Booking booking = bookingService.userOwnsBooking(userId.intValue(), bookingId);
    if (booking == null) {
      // TODO: implement not found
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    travelerService.addTravelerToBooking(traveler, booking);
    return ResponseEntity.created(new URI("/traveler/id")).body(traveler);
  }
}
