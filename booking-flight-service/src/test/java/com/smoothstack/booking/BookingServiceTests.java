package com.smoothstack.booking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.smoothstack.booking.dao.BookingDao;
import com.smoothstack.booking.dao.FlightDao;
import com.smoothstack.booking.dao.TravelerDao;
import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.Flight;
import com.smoothstack.booking.entity.Traveler;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;
import com.smoothstack.booking.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookingServiceTests {

  @Mock
  private BookingDao bookingDao;

  @Mock
  private FlightDao flightDao;

  @Mock
  private TravelerDao travelerDao;

  @InjectMocks
  private BookingService bookingService;

  private final Long NOT_FOUND = 1L;
  private final Long BOOKING_ID = 2L;
  private final Long FLIGHT_ID = 3L;
  private final Integer TRAVELER_ID = 4;
  private final Integer BOOKER_ID = 5;
  private final String STRIPE_ID = "payment id";
  private final String UPDATED_STRIPE_ID = "pay";
  private final Flight FLIGHT = new Flight(
    FLIGHT_ID,
    LocalDateTime.now(),
    LocalDateTime.now(),
    100,
    99.50,
    "123",
    null
  );
  private final Traveler TRAVELER = new Traveler(
    "Test Traveler",
	  "123 Example Rd",
    "800-123-4567",
    "abc@def.com",
    LocalDateTime.now()
  );
  private final Booking BOOKING = new Booking(BOOKING_ID, true, STRIPE_ID, BOOKER_ID, List.of(), Set.of());
  private final Booking BOOKING_NOT_FOUND = new Booking(
    NOT_FOUND,
    true,
    "abc",
    BOOKER_ID,
    List.of(),
    Set.of()
  );
  private final Booking UPDATED_BOOKING = new Booking(
    BOOKING_ID,
    false,
    UPDATED_STRIPE_ID,
    BOOKER_ID,
    List.of(),
    Set.of()
  );

  @BeforeEach
  void setBookingDaoOutput() {
    when(bookingDao.findById(NOT_FOUND)).thenReturn(Optional.empty());
    when(bookingDao.findById(BOOKING_ID)).thenReturn(Optional.of(BOOKING));
    when(bookingDao.save(BOOKING)).thenReturn(BOOKING);
    when(bookingDao.findAll()).thenReturn(List.of(BOOKING));
    when(bookingDao.findByBookerId(BOOKER_ID)).thenReturn(List.of(BOOKING));
  }

  @BeforeEach
  void setFlightDaoOutput() {
    when(flightDao.findById(NOT_FOUND)).thenReturn(Optional.empty());
    when(flightDao.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
  }

  @BeforeEach
  void setTravelerDaoOutput() {
    when(travelerDao.findById(NOT_FOUND.intValue())).thenReturn(Optional.empty());
    when(travelerDao.findById(TRAVELER_ID)).thenReturn(Optional.of(TRAVELER));
  }
  
  @Test
  @DisplayName("get booking by id, booking not found, expect exception")
  void test1() {
    assertThrows(ResourceNotFoundException.class, () -> {
      bookingService.getBookingById(NOT_FOUND);
    });
  }

  @Test
  @DisplayName("get booking by id, expect booking body")
  void test2() {
    Booking booking = bookingService.getBookingById(BOOKING_ID);
    assertThat(BOOKING, samePropertyValuesAs(booking));
  }

  @Test
  @DisplayName("create booking invalid flight, exception thrown")
  void test3() {
    assertThrows(Exception.class, () -> {
      bookingService.createBooking(BOOKING);
    });
  }

  @Test
  @DisplayName("create booking invalid traveler, exception thrown")
  void test4() {
    assertThrows(Exception.class, () -> {
      bookingService.createBooking(BOOKING);
    });
  }

  @Test
  @DisplayName("update booking with new information")
  void test8() {
    Booking booking = bookingService.updateBooking(UPDATED_BOOKING);
    assertEquals(UPDATED_BOOKING.getIsActive(), booking.getIsActive());
    assertEquals(UPDATED_BOOKING.getStripeId(), booking.getStripeId());
  }

  @Test
  @DisplayName("update booking, booking id not found")
  void test9() {
    assertThrows(ResourceNotFoundException.class, () -> {
      bookingService.updateBooking(BOOKING_NOT_FOUND);
    });
  }

  @Test
  @DisplayName("delete booking, booking id not found")
  void test10() {
    assertThrows(ResourceNotFoundException.class, () -> {
      bookingService.deleteBooking(NOT_FOUND.intValue());
    });
  }

  @Test
  @DisplayName("delete booking")
  void test11() {
    bookingService.deleteBooking(BOOKING_ID.intValue());
    verify(bookingDao).delete(BOOKING);
  }

  @Test
  @DisplayName("get all bookings")
  void test12() {
    List<Booking> bookings = bookingService.getAllBookings();
    assertEquals(BOOKING, bookings.get(0));
  }

  @Test
  @DisplayName("get all bookings by user id")
  void test13() {
    List<Booking> bookings = bookingService.getByUserId(BOOKER_ID);
    assertEquals(BOOKING, bookings.get(0));
  }

  @Test
  @DisplayName("user owns booking, expect false")
  void test14() {
    assertEquals(null, bookingService.userOwnsBooking(BOOKER_ID + 1, BOOKING_ID.intValue()));
  }

  @Test
  @DisplayName("user owns booking, expect true")
  void test15() {
    assertEquals(
      BOOKING,
      bookingService.userOwnsBooking(BOOKER_ID, BOOKING_ID.intValue())
    );
  }

  @Test
  @DisplayName("user owns booking, booking not found")
  void test16() {
    assertEquals(null, bookingService.userOwnsBooking(BOOKER_ID, NOT_FOUND.intValue()));
  }
}
