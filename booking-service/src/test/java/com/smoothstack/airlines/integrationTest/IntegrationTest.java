package com.smoothstack.airlines.integrationTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.smoothstack.airlines.controller.BookingController;
import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.exceptions.ResourceNotFoundException;
import com.smoothstack.airlines.exceptions.ResourceExistsException;


/*
 * Runs integration tests on all bookings endpoints. Uses a local MySQL database instance
 * and deletes all bookings prior to running tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTest {
	
	@Autowired
	private ServletWebServerApplicationContext appContext;
	
	@Autowired
	private BookingController bookingController;
	
	private HttpClient http = HttpClient.newHttpClient();
	
	@DisplayName("delete all bookings")
	@Test
	@Order(1)
	void deleteAllBookings() throws Exception {
		List<Booking> bookings = bookingController.getBookings();
		
		bookings.forEach(booking -> {
			try {
				bookingController.deleteBooking(booking.getBookingId());
			} catch (ResourceNotFoundException e) {
				fail("Resource not found exception");
			}
		});
	}
	
	@DisplayName("get all bookings, returns empty list")
	@Test
	@Order(2)
	void getAllBookings() {
		List<Booking> bookings = bookingController.getBookings();
		
		assertEquals(bookings.size(), 0);
	}
	
	@DisplayName("create a booking for a traveler that does not exist")
	@Test
	@Order(3)
	void createBookingInvalidTravelerId() throws Exception {
		Booking booking = new Booking(1, 1, true, "1", 1);
		
		assertThrows(ResourceNotFoundException.class, () -> bookingController.createBooking(booking, 9999));
	}
	
	@DisplayName("create booking for traveler 1, id 96")
	@Test
	@Order(4)
	void createBooking96() throws Exception {
		Booking booking = new Booking(96, 1, true, "1", 1);
		ResponseEntity<Booking> response = bookingController.createBooking(booking, 1);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody(), booking);
		assertEquals(response.getHeaders().getLocation().getPath(), "/bookings/96");
	}
	
	@DisplayName("update booking 96, change active to false")
	@Test
	@Order(5)
	void updateBookingActive() throws Exception {
		Booking booking = new Booking(96, 1, false, "1", 1);
		ResponseEntity<Booking> response = bookingController.updateBooking(booking);
		List<Booking> bookings = bookingController.getAllBookings(1);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), booking);
		assertEquals(bookings.size(), 1);
		assertEquals(bookings.get(0).getIsActive(), false);
	}
	
	@DisplayName("create booking traveler 1, id 97")
	@Test
	@Order(6)
	void createBooking97() throws Exception {
		Booking booking = new Booking(97, 1, true, "1", 1);
		ResponseEntity<Booking> response = bookingController.createBooking(booking, 1);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody(), booking);
	}
	
	@DisplayName("create booking traveler 2, id 98")
	@Test
	@Order(7)
	void createBooking98() throws Exception {
		Booking booking = new Booking(98, 1, true, "4325432", 1);
		ResponseEntity<Booking> response = bookingController.createBooking(booking, 2);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody(), booking);
	}
	
	@DisplayName("get all bookings for traveler 1")
	@Test
	@Order(8)
	void getBookings1() throws Exception {
		List<Booking> bookings = bookingController.getAllBookings(1);
		
		assertEquals(bookings.size(), 2);
		Integer firstId = bookings.get(0).getBookingId();
		Integer secondId = bookings.get(1).getBookingId();
		
		if (!((firstId == 96 && secondId == 97) || (firstId == 97 && secondId == 96))) {
			fail("wrong ids");
		}
	}
	
	@DisplayName("get all bookings for traveler 2")
	@Test
	@Order(9)
	void getBookings2() throws Exception {
		List<Booking> bookings = bookingController.getAllBookings(2);
		
		assertEquals(bookings.size(), 1);
		assertEquals(bookings.get(0).getBookingId(), 98);
	}
	
	@DisplayName("delete booking 96 for traveler 1")
	@Test
	@Order(10)
	void deleteBooking96() throws Exception {
		ResponseEntity<String> response = bookingController.deleteBooking(96);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		List<Booking> bookings = bookingController.getAllBookings(1);
		assertEquals(bookings.size(), 1);
		assertEquals(bookings.get(0).getBookingId(), 97);
	}
	
	@DisplayName("add booking 97 that already exists")
	@Test
	@Order(11)
	void addBookingAlreadyExists() throws Exception {
		Booking booking = new Booking(97, 1, false, "asdasd", 1);
		assertThrows(ResourceExistsException.class, () -> bookingController.createBooking(booking, 1));
	}
}
