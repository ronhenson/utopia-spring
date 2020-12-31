package com.smoothstack.airlines.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//import java.net.http.HttpClient;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.smoothstack.airlines.controller.BookingController;
import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.BookingRequest;
import com.smoothstack.airlines.exceptions.ResourceExistsException;
import com.smoothstack.airlines.exceptions.ResourceNotFoundException;


/*
 * Runs integration tests on all bookings endpoints. Uses a local MySQL database instance
 * and deletes all bookings prior to running tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTest {
	
//	@Autowired
//	private ServletWebServerApplicationContext appContext;
	
	@Autowired
	private BookingController bookingController;
	
//	private HttpClient http = HttpClient.newHttpClient();
	
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
		Booking booking = new Booking(true, "1", 1);
		BookingRequest request = new BookingRequest(booking, List.of(999));
		
		
		assertThrows(ResourceNotFoundException.class, () -> bookingController.createBooking(request, 9999));
	}
	
	@DisplayName("create booking for traveler 1, id 96")
	@Test
	@Order(4)
	void createBooking96() throws Exception {
		Booking booking = new Booking(true, "1", 1);
		BookingRequest request = new BookingRequest(booking, List.of(96));
		ResponseEntity<Booking> response = bookingController.createBooking(request, 1);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody(), booking);
		assertEquals(response.getHeaders().getLocation().getPath(), "/bookings/96");
	}
	
	@DisplayName("update booking 96, change active to false")
	@Test
	@Order(5)
	void updateBookingActive() throws Exception {
		Booking booking = new Booking(false, "1", 1);
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
		Booking booking = new Booking(true, "1", 1);
		BookingRequest request = new BookingRequest(booking, List.of(999));
		ResponseEntity<Booking> response = bookingController.createBooking(request, 1);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody(), booking);
	}
	
	@DisplayName("create booking traveler 2, id 98")
	@Test
	@Order(7)
	void createBooking98() throws Exception {
		Booking booking = new Booking(true, "4325432", 1);
		BookingRequest request = new BookingRequest(booking, List.of(999));
		ResponseEntity<Booking> response = bookingController.createBooking(request, 2);
		
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
		Booking booking = new Booking(false, "asdasd", 1);
		BookingRequest request = new BookingRequest(booking, List.of(999));
		assertThrows(ResourceExistsException.class, () -> bookingController.createBooking(request, 1));
	}
}
