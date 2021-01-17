package com.smoothstack.airlines.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:h2-database.properties")
public class GetBookingsTest {
  @Autowired
  MockMvc mockMvc;

  String URL = "/booking";

  @DisplayName("get all bookings, not logged in, should return unauthorized")
  @Test
  void test1() throws Exception {
    mockMvc.perform(get(URL)).andExpect(status().isUnauthorized());
  }

  @DisplayName("get all bookings, logged in user with no bookings, returns ok and empty list")
  @Test
  void test2() throws Exception {
    mockMvc.perform(get(URL).header("user-id", 1))
    .andExpect(status().isOk())
    .andExpect(content().string("[]"));
  }

  @DisplayName("get all bookings, logged in employee, should return all bookings for all users")
  @Test
  void test3() throws Exception {
    mockMvc.perform(get(URL).header("user-id", 3))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)));
  }

  @DisplayName("get all bookings, logged in user, returns list of bookings")
  @Test
  void test4() throws Exception {
    mockMvc.perform(get(URL).header("user-id", 4))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].bookingId").value("5"));
  }
}
