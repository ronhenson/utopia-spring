package com.smoothstack.orchestrator.integration;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.smoothstack.orchestrator.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = { "classpath:h2-database.properties", "classpath:mail-server.properties" })
public class AuthenticationTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  JavaMailSender emailSender;

  @RegisterExtension
  static GreenMailExtension greenmail = new GreenMailExtension(ServerSetupTest.SMTP)
    .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "password"))
      .withPerMethodLifecycle(false);

  public String asJsonString(final Object obj) {
    try {
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

  @DisplayName("sign up with already taken email")
  @Test
  @Order(5)
  void test5() throws Exception {
    User user = new User("Derek", "Lance", "password", "abc@def.com");
    mockMvc.perform(post("/auth/sign-up").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("confirm an account")
  @Test
  @Order(6)
  void test6() throws Exception {
    User user = new User("Derek", "Lance", "password", "abc6@def.com");
    mockMvc.perform(post("/auth/sign-up").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON));
    Message[] messages = greenmail.getReceivedMessages();
    String emailMessage = GreenMailUtil.getBody(messages[messages.length - 1]);
    Integer tokenStart = emailMessage.indexOf("?token=");
    String token = emailMessage.substring(tokenStart);
    mockMvc.perform(get("/auth/confirm" + token)).andExpect(status().isOk());
    mockMvc.perform(get("/auth/confirm" + token)).andExpect(status().isBadRequest());
  }

  @DisplayName("login with unconfirmed account")
  @Test
  @Order(7)
  void test7() throws Exception {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", "not@confirmed.com");
    credentials.put("password", "pass");
    mockMvc.perform(post("/auth/login")
      .content(asJsonString(credentials))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  @DisplayName("login normally with a confirmed account")
  @Test
  @Order(9)
  void test9() throws Exception {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", "abc6@def.com");
    credentials.put("password", "password");
    mockMvc.perform(post("/auth/login")
      .content(asJsonString(credentials))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("token")));
  }

  @DisplayName("login with a confirmed account, bad credentials")
  @Test
  @Order(10)
  void test10() throws Exception {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", "abc6@def.com");
    credentials.put("password", "password1");
    mockMvc.perform(post("/auth/login").content(asJsonString(credentials)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }
  
  @DisplayName("login with a confirmed account, too many fields")
  @Test
  @Order(11)
  void test11() throws Exception {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", "abc6@def.com");
    credentials.put("password", "password1");
    credentials.put("bad", "field");
    mockMvc.perform(post("/auth/login")
      .content(asJsonString(credentials))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
}
