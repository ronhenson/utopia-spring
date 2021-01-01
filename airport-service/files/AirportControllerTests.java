/**
 * 
 */
package com.ss.UtopiaAirlines;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ss.UtopiaAirlines.exceptions.ResourceDoesNotExistsException;

/**
 * @author ronh
 *
 */
class AirportControllerTests extends AirportApplicationTests {

	/**
	 * @throws java.lang.Exception
	 */
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void test() {
		try {
			mockMvc.perform(get("/id")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.airportId").value(1))
				.andExpect(jsonPath("$.city").value("San Francisco"));
		} catch (ResourceDoesNotExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	

}
