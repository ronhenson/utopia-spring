package com.smoothstack.orchestrator;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.orchestrator.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AuthenticationIntegrationTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      System.out.println(jsonContent);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @DisplayName("sign up with empty request body, expect 400")
  @Test
  @Order(1)
  void test1() throws Exception {
    mockMvc.perform(post("/auth/sign-up")).andExpect(status().isBadRequest());
  }

  @DisplayName("sign up with valid request, expects 201 with Location header")
  @Test
  @Order(2)
  void test2() throws Exception {
    User user = new User("Derek", "Lance", "password", "abc@def.com");
    mockMvc.perform(post("/auth/sign-up").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }
  
  @DisplayName("sign up with missing fields")
  @Test
  @Order(3)
  void test3() throws Exception {
    Map<String, String> json = new HashMap<>();
    json.put("email", "test@abc.com");
    json.put("password", "test_password");
    mockMvc.perform(post("/auth/sign-up").content(asJsonString(json)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
  
  @DisplayName("sign up with too many fields")
  @Test
  @Order(4)
  void test4() throws Exception {
    Map<String, String> json = new HashMap<>();
    json.put("email", "test@abc.com");
    json.put("password", "pass");
    json.put("firstName", "first");
    json.put("lastName", "last");
    json.put("badFieldName", "bad");
    mockMvc.perform(post("/auth/sign-up").content(asJsonString(json)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
