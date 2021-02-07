package com.smoothstack.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.BookingRequest;

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
  private final Integer USER_ID_NO_BOOKINGS = 3;
  private final Integer FLIGHT_ID = 1;
  private final Integer TRAVELER_ID = 1;
  private final Long CREATED_BOOKING_ID = 4L;

  @DisplayName("test get booking by id, expect 200")
  @Test
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
  void testGetBookingList() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "USER")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)))
      .andExpect(jsonPath("$[0].bookingId", is(BOOKING_ID)));
  }

  @DisplayName("test get booking by id, booking does not exist, expect empty list")
  @Test
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
  void testGetBookingListEmployee() throws Exception {
    mockMvc.perform(
      get("/booking")
        .header("user-id", LOGGED_IN_USER_ID)
        .header("user-role", "EMPLOYEE")
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(4)));
  }
  
  @DisplayName("test get booking list, nonexistent role, expect bad request")
  @Test
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
  @Order(1)
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
  @Order(1)
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
  @Order(1)
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
  @Order(1)
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
}
