package com.smoothstack.airlines.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
	
	@InjectMocks
	private BookingController bookingController;
	
	@Mock
	private BookingService bookingService;

	@DisplayName("Get all bookings")
	@Test
	void testGetAllBookings() throws Exception {
		Booking booking1 = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg")
			.bookerId(21).build();
		Booking booking2 = Booking.builder().bookingId(2).flightId(5).isActive(false).stripeId("123456")
			.bookerId(56).build();
		
		List<Booking> mockedBookings = List.of(booking1, booking2);
		 when(bookingService.getBookingsByTraveler(1)).thenReturn(mockedBookings);
		 List<Booking> bookings = bookingController.getAllBookings(1);
		 verify(bookingService).getBookingsByTraveler(1);
		 assertThat(bookings, containsInAnyOrder(booking1, booking2));
	}
	
	@DisplayName("Create new booking for traveler id")
	@Test
	void testCreateBooking() throws Exception {
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg")
				.bookerId(21).build();
		
		ResponseEntity<Booking> response = bookingController.createBooking(booking, 1);
		assertThat(response.getBody(), is(booking));
		assertThat(response.getHeaders().getLocation().getPath(), is("/bookings/1"));
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
	}

	@DisplayName("Update booking by id")
	@Test
	void testUpdateBooking() throws Exception {
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg")
				.bookerId(21).build();
		
		ResponseEntity<Booking> response = bookingController.updateBooking(booking);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is(booking));
	}
	
	@DisplayName("Delete booking by id")
	@Test
	void testDeleteBooking() throws Exception {
		doNothing().when(bookingService).deleteBooking(1);
		ResponseEntity<String> response = bookingController.deleteBooking(1);
		verify(bookingService).deleteBooking(1);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is("Booking deleted successfully."));
	}
}
