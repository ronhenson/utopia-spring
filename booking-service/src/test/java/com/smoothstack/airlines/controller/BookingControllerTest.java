package com.smoothstack.airlines.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.smoothstack.airlines.entity.BookingRequest;
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
		Booking booking1 = Booking.builder().bookingId(1).isActive(true).stripeId("abcdefg")
			.bookerId(21).build();
		Booking booking2 = Booking.builder().bookingId(2).isActive(false).stripeId("123456")
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
		Booking booking = Booking.builder().isActive(true).stripeId("abcdefg")
				.bookerId(1).build();
		
		BookingRequest request = new BookingRequest(booking, new ArrayList<Integer>());
		ResponseEntity<Booking> response = bookingController.createBooking(request, 1);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
	}

	@DisplayName("Update booking by id")
	@Test
	void testUpdateBooking() throws Exception {
		Booking booking = Booking.builder().bookingId(1).isActive(true).stripeId("abcdefg")
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
