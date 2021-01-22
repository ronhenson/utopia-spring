package com.smoothstack.airlines.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.BookingRequest;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:h2-database.properties")
public class CreateBookingTests {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  Booking booking = new Booking(true, "payment");

  String URL = "/booking/flight/";

  String USER_ID_HEADER = "user-id";

  public String asJsonString(final Object obj) {
    try {
      final String jsonContent = mapper.writeValueAsString(obj);
      System.out.println(jsonContent);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @DisplayName("create new booking, not logged in, return unauthorized")
  @Test
  void test1() throws Exception {
    mockMvc.perform(post(URL + "2")
      .content(asJsonString(booking))
      .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isUnauthorized());
  }

  @DisplayName("create new booking, not confirmed user, return forbidden")
  @Test
  void test2() throws Exception {
    mockMvc.perform(post(URL + "2")
      .content(asJsonString(booking))
      .header(USER_ID_HEADER, 5))
    .andExpect(status().isForbidden());
  }

  // TODO: check seats available on flight
  @DisplayName("create new booking, logged in, return 201 and location header")
  @Test
  void test3() throws Exception {
    mockMvc.perform(post(URL + "4")
      .header(USER_ID_HEADER, 1)
      .content(asJsonString(booking))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("create new booking, logged in, return 400 when not enough seats available")
  @Test
  void test4() throws Exception {
    BookingRequest booking = new BookingRequest();
    booking.setIsActive(true);
    booking.setStripeId("payment");
    booking.setTravelerIds(List.of(3, 8));
    mockMvc.perform(get(URL)
      .header(USER_ID_HEADER, 4)
      .content(asJsonString(booking))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
}
