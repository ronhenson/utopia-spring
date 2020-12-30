package com.smoothstack.airlines.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smoothstack.airlines.dao.BookingDao;
import com.smoothstack.airlines.dao.BookingsHasTravelersDao;
import com.smoothstack.airlines.dao.FlightDao;
import com.smoothstack.airlines.dao.TravelerDao;
import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.BookingsHasTravelers;
import com.smoothstack.airlines.entity.Flight;
import com.smoothstack.airlines.entity.Traveler;
import com.smoothstack.airlines.entity.primaryKeys.BookingKey;
import com.smoothstack.airlines.exceptions.ResourceExistsException;
import com.smoothstack.airlines.exceptions.ResourceNotFoundException;
import com.smoothstack.constants.ResourceType;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

	@InjectMocks
	private BookingService bookingService;

	@Mock
	private BookingDao bookingDao;

	@Mock
	private FlightDao flightDao;

	@Mock
	private TravelerDao travelerDao;

	@Mock
	private BookingsHasTravelersDao bookingsHasTravelersDao;

	@DisplayName("Get all bookings, throw exception on traveler id not found")
	@Test
	void testGetBookingsByTravelerException() throws Exception {
		when(travelerDao.findById(0)).thenReturn(Optional.empty());
		
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingsByTraveler(0));
		assertThat(thrown.getResourceId(), is(0));
		assertThat(thrown.getResourceType(), is(ResourceType.TRAVELER));
		
		verify(travelerDao).findById(0);
	}
	
	@DisplayName("Get all bookings by traveler id")
	@Test
	void testGetBookingsByTraveler() throws Exception {
		Booking booking1 = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		Booking booking2 = Booking.builder().bookingId(2).flightId(5).isActive(false).stripeId("123456").bookerId(56)
				.build();
		BookingsHasTravelers bookingTraveler1 = new BookingsHasTravelers(1, 1, 1);
		BookingsHasTravelers bookingTraveler2 = new BookingsHasTravelers(2, 5, 1);
		BookingKey bookingKey1 = new BookingKey(1, 1);
		BookingKey bookingKey2 = new BookingKey(2, 5);
	
		when(travelerDao.findById(1)).thenReturn(Optional.of(new Traveler()));
		when(bookingsHasTravelersDao.findByTravelerTravelerId(1)).thenReturn(Set.of(bookingTraveler1, bookingTraveler2));
		when(bookingDao.findAllById(Set.of(bookingKey1, bookingKey2))).thenReturn(List.of(booking1, booking2));
		
		assertThat(bookingService.getBookingsByTraveler(1), containsInAnyOrder(booking1, booking2));
		
		verify(travelerDao).findById(1);
		verify(bookingsHasTravelersDao).findByTravelerTravelerId(1);
		verify(bookingDao).findAllById(Set.of(bookingKey1, bookingKey2));
	}
	
	@DisplayName("Create new booking, throw exception resource already exists")
	@Test
	void testCreateBookingAlreadyExists() throws Exception {
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		when(bookingDao.findByBookingId(booking.getBookingId())).thenReturn(booking);
		
		ResourceExistsException thrown = assertThrows(ResourceExistsException.class, () -> bookingService.createBooking(booking, 1));
		assertThat(thrown.getResourceId(), is(1));
		assertThat(thrown.getResourceType(), is(ResourceType.BOOKING));
		
		verify(bookingDao).findByBookingId(booking.getBookingId());
	}
	
	@DisplayName("Create new booking, throw exception traveler does not exist")
	@Test
	void testCreateBookingNoTraveler() throws Exception {
		Integer travelerId = 99;
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		when(bookingDao.findByBookingId(booking.getBookingId())).thenReturn(null);
		when(travelerDao.findById(travelerId)).thenReturn(Optional.empty());
		
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(booking, travelerId));
		assertThat(thrown.getResourceId(), is(travelerId));
		assertThat(thrown.getResourceType(), is(ResourceType.TRAVELER));
		
		verify(bookingDao).findByBookingId(booking.getBookingId());
		verify(travelerDao).findById(travelerId);
	}
	
	@DisplayName("Create new booking")
	@Test
	void testCreateBooking() throws Exception {
		Integer travelerId = 99;
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		Timestamp time = Timestamp.from(Instant.now());
		Flight flight = new Flight(1, time, 2, 2, 200, 250f);
		Traveler traveler = new Traveler(travelerId, "Derek", "123 First St", "111", "a@b.c", Timestamp.from(Instant.now()));
		
		when(bookingDao.findByBookingId(booking.getBookingId())).thenReturn(null);
		when(travelerDao.findById(travelerId)).thenReturn(Optional.of(traveler));
		when(flightDao.findByFlightId(booking.getFlightId())).thenReturn(flight);
		when(bookingDao.save(booking)).thenReturn(booking);
		
		Booking savedBooking = bookingService.createBooking(booking, travelerId);
		assertThat(savedBooking, is(booking));
		assertThat(savedBooking.getTravelers(), contains(traveler));
		assertThat(savedBooking.getFlight(), is(flight));
		
		verify(bookingDao).findByBookingId(booking.getBookingId());
		verify(travelerDao).findById(travelerId);
		verify(flightDao).findByFlightId(booking.getFlightId());
		verify(bookingDao).save(booking);
	}
	
	@DisplayName("Update booking, throw exception resource not found")
	@Test
	void testUpdateBookingNotFound() throws Exception {
		Booking booking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		when(bookingDao.findByBookingId(booking.getBookingId())).thenReturn(null);
		
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> bookingService.updateBooking(booking));
		assertThat(thrown.getResourceId(), is(booking.getBookingId()));
		assertThat(thrown.getResourceType(), is(ResourceType.BOOKING));
		
		verify(bookingDao).findByBookingId(booking.getBookingId());
	}
	
	@DisplayName("Update booking")
	@Test
	void testUpdateBooking() throws Exception {
		Booking oldBooking = Booking.builder().bookingId(1).flightId(1).isActive(true).stripeId("abcdefg").bookerId(21)
				.build();
		Booking newBooking = Booking.builder().bookingId(1).flightId(5).isActive(false).stripeId("123456").bookerId(56)
				.build();
		
		when(bookingDao.findByBookingId(newBooking.getBookingId())).thenReturn(oldBooking);
		when(bookingDao.save(oldBooking)).thenReturn(oldBooking);
		
		bookingService.updateBooking(newBooking);
		assertThat(oldBooking.getIsActive(), is(newBooking.getIsActive()));
		assertThat(oldBooking.getStripeId(), is(newBooking.getStripeId()));
		
		verify(bookingDao).findByBookingId(oldBooking.getBookingId());
		verify(bookingDao).save(oldBooking);
	}
}
