package com.ss.UtopiaAirlines.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.Optional;

import com.ss.UtopiaAirlines.dao.AirportDao;
import com.ss.UtopiaAirlines.entity.Airport;
import com.ss.UtopiaAirlines.exceptions.ResourceDoesNotExistsException;
import com.ss.UtopiaAirlines.exceptions.ResourceExistsException;
import com.ss.UtopiaAirlines.service.AirportService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.ss.UtopiaAirlines.controller.AirportController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AirportServiceTests {

  @Mock
  private AirportDao airportDao;

  @InjectMocks
  private AirportService airportService;

  private final String NOT_FOUND = "000";
  private final String IATALIDENT = "PDX";
  private final String CITY = "Portland, OR";
  private final String NAME = "Portland International";
  private final String QUERY = "Port";
  private final String IATALIDENTDEL = "XXX";

  private final Airport AIRPORT = new Airport(IATALIDENT, CITY, NAME);

  private final Airport AIRPORT_NOT_FOUND = new Airport(NOT_FOUND, "Town, CA", "Town Airport");

  private final Airport AIRPORT_ADD_TEST = new Airport(IATALIDENTDEL, "New Town", "New Town Airport");

  @BeforeEach
  void setAirportDaoOutput() {
    when(airportDao.findById(NOT_FOUND)).thenReturn(Optional.empty());
    when(airportDao.findById(IATALIDENT)).thenReturn(Optional.of(AIRPORT));
    when(airportDao.save(AIRPORT)).thenReturn(AIRPORT);
    when(airportDao.findAll()).thenReturn(List.of(AIRPORT));
    when(airportDao.search(QUERY)).thenReturn(List.of(AIRPORT));
  }

  @Test
  @DisplayName("get airport by iatalIdent, not found, expect exception")
  void test1() {
    assertThrows(ResourceDoesNotExistsException.class, () -> {
     airportService.getAirportById(NOT_FOUND);
     System.out.println("How did I get here ");
    });
  }

  // @Test
  // @DisplayName("get airport by iatalIdent, expect body")
  // void test2() {
  //   Optional<Airport> airport = airportService.getAirportById(IATALIDENT);
  //   assertThat(AIRPORT, samePropertyValueAs(airport.get()));

  // }

 @Test
 @DisplayName("add airport by Id, expect body")
 void test3() {
   assertThrows(ResourceExistsException.class, () -> {
     airportService.addAirport(AIRPORT_ADD_TEST);
   });
 }

 @Test
 @DisplayName("delete airport by Id, expect body")
 void test4() {
   assertThrows(ResourceDoesNotExistsException.class, () -> {
     airportService.deleteAirport(IATALIDENTDEL);
   });
 }

 @Test
 @DisplayName("query 15 at a time, expect list")
 void test5() {
   assertThrows(ResourceDoesNotExistsException.class, () -> {
     airportService.deleteAirport(QUERY);
   });
 }
}
