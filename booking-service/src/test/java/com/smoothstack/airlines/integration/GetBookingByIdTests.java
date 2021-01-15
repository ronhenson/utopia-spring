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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:h2-database.properties")
public class GetBookingByIdTests {

  @Autowired
  MockMvc mockMvc;

  String URL = "/booking/";

  @DisplayName("get booking by id, not logged in, should return unauthorized")
  @Test
  void test1() throws Exception {
    mockMvc.perform(get(URL + "4")).andExpect(status().isUnauthorized());
  }

  @DisplayName("get booking by id, logged in user does not own booking, should return forbidden")
  @Test
  void test2() throws Exception {
    mockMvc.perform(get(URL + "4").header("user-id", 1))
    .andExpect(status().isForbidden());
  }
  
  @DisplayName("get booking by id, logged in as employee, should return status ok and booking")
  @Test
  void test3() throws Exception {
    mockMvc.perform(get(URL + "4").header("user-id", 3))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.bookingId").value("4"));
  }
  
  @DisplayName("get booking by id, logged in, should return status ok and booking")
  @Test
  void test4() throws Exception {
    mockMvc.perform(get(URL + "4").header("user-id", 2))
      .andExpect(status().isOk())
        .andExpect(jsonPath("$.bookingId").value("4"));
  }
}

