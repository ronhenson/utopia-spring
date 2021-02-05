package com.smoothstack.utopia;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.utopia.entity.User;

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

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = { "classpath:h2-database.properties" })
public class IntegrationTests {
  
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @DisplayName("test update user by id, expect 409 conflict email already taken")
  @Test
  @Order(1)
  void testUpdateByIdEmailConflict() throws Exception {
    User user = new User(4L, "newFirst", "newLast", "password", "user@1.com", 0);
    String userJson = objectMapper.writeValueAsString(user);
    mockMvc.perform(
      put("/users")
        .content(userJson)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isConflict());
  }
  
  @DisplayName("test update user by id change role, expect unauthorized")
  @Test
  @Order(2)
  void testUpdateByIdChangeRoleUnauthorized() throws Exception {
    User user = new User(4L, "newFirst", "newLast", "password", "user@0.com", 1);
    String userJson = objectMapper.writeValueAsString(user);
    mockMvc.perform(
      put("/users")
        .content(userJson)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isUnauthorized());
  }
  
  @DisplayName("test update user by id, expect 200 ok")
  @Test
  @Order(3)
  void testUpdateById() throws Exception {
    User user = new User(4L, "newFirst", "newLast", "password", "user@4.com", 0);
    String userJson = objectMapper.writeValueAsString(user);
    mockMvc.perform(
      put("/users")
        .content(userJson)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.firstName", is("newFirst")))
      .andExpect(jsonPath("$.lastName", is("newLast")))
      .andExpect(jsonPath("$.password", is("password")));
  }

  @DisplayName("test delete user by id, expect 200 ok")
  @Test
  @Order(4)
  void testDeleteById() throws Exception {
    mockMvc.perform(delete("/users/4"))
      .andExpect(status().isOk());
    mockMvc.perform(get("/users?findall=true"))
      .andExpect(jsonPath("$", hasSize(3)));
    mockMvc.perform(get("/users/4"))
      .andExpect(status().isNotFound());
  }
  
  @DisplayName("test delete user by id, expect 404 not found")
  @Test
  void testDeleteByIdNotFound() throws Exception {
    mockMvc.perform(delete("/users/0"))
      .andExpect(status().isBadRequest());
  }

  @DisplayName("test find all users, expect 200, json response, correct size")
  @Test
  void testFindAll() throws Exception {
    mockMvc.perform(get("/users?findall=true"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$", hasSize(3)));
  }
  
  @DisplayName("test find by user id admin search, expect 200")
  @Test
  void testAdminFindById() throws Exception {
    mockMvc.perform(get("/users?userId=1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].userId", is(1)))
      .andExpect(jsonPath("$", hasSize(1)));
  }
  
  @DisplayName("test find by user id does not exist admin search, expect 404")
  @Test
  void testAdminFindNotFound() throws Exception {
    mockMvc.perform(get("/users?userId=0"))
      .andExpect(status().isNotFound());
  }
  
  @DisplayName("test find by first name and last name")
  @Test
  void testFindByFirstAndLast() throws Exception {
    mockMvc.perform(get("/users?firstName=user&lastName=one"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$", hasSize(1)))
    .andExpect(jsonPath("$[0].firstName", is("user")))
    .andExpect(jsonPath("$[0].lastName", is("one")));
  }
  
  @DisplayName("test find by first name")
  @Test
  void testFindByFirst() throws Exception {
    mockMvc.perform(get("/users?firstName=user"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$", hasSize(2)))
    .andExpect(jsonPath("$[0].firstName", is("user")))
    .andExpect(jsonPath("$[1].firstName", is("user")));
  }
  
  @DisplayName("test find by last name")
  @Test
  void testFindByLast() throws Exception {
    mockMvc.perform(get("/users?lastName=one"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].firstName", is("user")))
      .andExpect(jsonPath("$[0].lastName", is("one")));
  }
  
  @DisplayName("test find user by email, expect 200 ok")
  @Test
  void testFindByEmail() throws Exception {
    mockMvc.perform(get("/users?email=user@1.com"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].email", is("user@1.com")));
  }
  
  @DisplayName("test find user by email, expect 404 not found")
  @Test
  void testFindByEmailNotFound() throws Exception {
    mockMvc.perform(get("/users?email=user@notfound.com"))
      .andExpect(status().isNotFound());
      
  }
  
  @DisplayName("test find user by first name, expect 404 not found")
  @Test
  void testFindByFirstNameNoResults() throws Exception {
    mockMvc.perform(get("/users?firstName=notFound"))
      .andExpect(status().isNotFound());
  }

  @DisplayName("test admin find user, bad param expect 400 bad request")
  @Test
  void testAdminBadParam() throws Exception {
    mockMvc.perform(get("/users?badParam=test"))
      .andExpect(status().isBadRequest());
  }
  
  @DisplayName("test find by user id, expect 200 ok")
  @Test
  void testFindById() throws Exception {
    mockMvc.perform(get("/users/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.firstName", is("user")))
      .andExpect(jsonPath("$.lastName", is("one")));
  }
  
  @DisplayName("test find by user id, expect 404 not found")
  @Test
  void testFindByIdNotFound() throws Exception {
    mockMvc.perform(get("/users/0")).andExpect(status().isNotFound());
  }
}
