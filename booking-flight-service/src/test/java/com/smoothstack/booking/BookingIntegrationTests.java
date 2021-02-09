package com.smoothstack.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.BookingRequest;
import com.smoothstack.booking.entity.Traveler;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = { "classpath:h2-database.properties" })
public class BookingIntegrationTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  private final Integer BOOKING_ID = 1;
  private final Integer BOOKING_NOT_OWNED_ID = 2;
  private final Long LOGGED_IN_USER_ID = 1L;
  private final Long LOGGED_IN_USER_ID_NOT_AUTHORIZED= 5L;
  private final Integer USER_ID_NO_BOOKINGS = 5;
  private final Integer FLIGHT_ID = 1;
  private final Integer TRAVELER_ID = 1;
  private final Long CREATED_BOOKING_ID = 4L;

  @DisplayName("test get booking by id, expect 200")
  @Test
  @Order(1)
  void testGetBookingById() throws Exception {
    mockMvc.perform(
      get("/booking/" + BOOKING_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.bookingId", is(BOOKING_ID)));
  }

  @DisplayName("test get booking by id, user does not own booking, expect unauthorized")
  @Test
  @Order(1)
  void testGetBookingByIdUnauthorized() throws Exception {
    mockMvc.perform(
      get("/booking/" + BOOKING_NOT_OWNED_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isForbidden());
  }

  @DisplayName("test get booking by id, booking does not exist, expect not found")
  @Test
  @Order(1)
  void testGetBookingByIdNotFound() throws Exception {
    mockMvc.perform(
      get("/booking/" + "0")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isNotFound());
  }

  @DisplayName("test get booking by id, employee does not own booking, expect ok")
  @Test
  @Order(1)
  void testGetBookingByIdEmployee() throws Exception {
    mockMvc.perform(
      get("/booking/" + BOOKING_NOT_OWNED_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "EMPLOYEE")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.bookingId", is(BOOKING_NOT_OWNED_ID)));
  }

  @DisplayName("test get booking by id, employee does not own booking, expect ok")
  @Test
  @Order(1)
  void testGetBookingByIdAdmin() throws Exception {
    mockMvc.perform(
      get("/booking/" + BOOKING_NOT_OWNED_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "ADMIN")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.bookingId", is(BOOKING_NOT_OWNED_ID)));
  }

  @DisplayName("test get booking list, expect 200")
  @Test
  @Order(1)
  void testGetBookingList() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].bookingId", is(BOOKING_ID)));
  }

  @DisplayName("test get booking by id, booking does not exist, expect empty list")
  @Test
  @Order(1)
  void testGetBookingListNotFound() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", USER_ID_NO_BOOKINGS)
        .header("user-role", "USER")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(0)));
  }

  @DisplayName("test get booking list, employee, expect all bookings returned")
  @Test
  @Order(1)
  void testGetBookingListEmployee() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "EMPLOYEE")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)));
  }

  @DisplayName("test get booking list, nonexistent role, expect bad request")
  @Test
  @Order(1)
  void testGetBookingListBadRole() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "BAD_ROLE")
    )
      .andExpect(status().isBadRequest());
  }

  @DisplayName("test create booking, expect ok & bookerId = logged in userId")
  @Test
  @Order(3)
  void testCreateBookingSuccess() throws Exception {
    Booking booking = new Booking(true, "payment");
    BookingRequest bookingRequest = new BookingRequest(booking);
    mockMvc.perform(
      post("/booking/flight/" + FLIGHT_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
        .content(mapper.writeValueAsString(bookingRequest))
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.bookerId", is(LOGGED_IN_USER_ID.intValue())));
  }

  @DisplayName("test create booking, flight does not exist")
  @Test
  @Order(3)
  void testCreateBookingFlightNotFound() throws Exception {
    Booking booking = new Booking(true, "payment");
    BookingRequest bookingRequest = new BookingRequest(booking);
    mockMvc.perform(
      post("/booking/flight/0")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
        .content(mapper.writeValueAsString(bookingRequest))
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isNotFound());
  }

  @DisplayName("test create booking, traveler does not exist")
  @Test
  @Order(3)
  void testCreateBookingTravelerDoesNotExist() throws Exception {
    Booking booking = new Booking(true, "payment");
    BookingRequest bookingRequest = new BookingRequest(booking, List.of(0));
    mockMvc.perform(
      post("/booking/flight/" + FLIGHT_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
        .content(mapper.writeValueAsString(bookingRequest))
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isNotFound());
  }

  @DisplayName("test create booking, verify correct traveler ids and flight")
  @Test
  @Order(3)
  void testCreateBookingVerifyTravelersAndFlight() throws Exception {
    Booking booking = new Booking(CREATED_BOOKING_ID, true, "payment", LOGGED_IN_USER_ID.intValue(), List.of(), Set.of());
    BookingRequest bookingRequest = new BookingRequest(booking, List.of(TRAVELER_ID));
    MvcResult result = mockMvc.perform(
      post("/booking/flight/" + FLIGHT_ID)
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
        .content(mapper.writeValueAsString(bookingRequest))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();
    mockMvc.perform(
      get(result.getResponse().getHeader("Location"))
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.flights", hasSize(1)))
      .andExpect(jsonPath("$.flights[0].flightId", is(FLIGHT_ID)))
      .andExpect(jsonPath("$.travelers", hasSize(1)))
      .andExpect(jsonPath("$.travelers[0].travelerId", is(TRAVELER_ID)));
  }

  @DisplayName("test update booking, expect  ")
  @Test
  @Order(4)
  void testUpdateBooking() throws Exception {
    Booking booking = new Booking(BOOKING_ID.longValue(), false, "payment", LOGGED_IN_USER_ID.intValue(), List.of(), Set.of());
    // BookingRequest bookingRequest = new BookingRequest(booking, List.of(TRAVELER_ID));
    mockMvc.perform(
      put("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "EMPLOYEE")
        .content(mapper.writeValueAsString(booking))
        .contentType(MediaType.APPLICATION_JSON)
      )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isActive", is(false)));
  }

  @DisplayName("test delete booking, expect OK   ")
  @Test
  @Order(6)
  void testDeleteBooking() throws Exception {
    mockMvc.perform(
      delete("/booking/" + "1")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
      )
        .andExpect(status().isOk());
  }

  @DisplayName("test delete booking, expect not found")
  @Test
  @Order(7)
  void testDeleteBookingNotFound() throws Exception {
    mockMvc.perform(
      delete("/booking/" + "5")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
      )
        .andExpect(status().isNotFound());
  }

  @DisplayName("test delete booking, expect forbidden")
  @Test
  @Order(5)
  void testDeleteBookingNotAuthorized() throws Exception {
    mockMvc.perform(
      delete("/booking/" + BOOKING_NOT_OWNED_ID)
        .header("user-id", LOGGED_IN_USER_ID_NOT_AUTHORIZED)
        .header("user-role", "USER")
      )
        .andExpect(status().isForbidden());
  }

  @DisplayName("test delete booking with user role EMPLOYEE, expect 200")
  @Test
  @Order(6)
  void testDeleteBookingWithEmployeeRole() throws Exception {
    mockMvc.perform(
      delete("/booking/" + BOOKING_NOT_OWNED_ID)
        .header("user-id", LOGGED_IN_USER_ID_NOT_AUTHORIZED)
        .header("user-role", "EMPLOYEE")
      )
        .andExpect(status().isOk());
  }
}
