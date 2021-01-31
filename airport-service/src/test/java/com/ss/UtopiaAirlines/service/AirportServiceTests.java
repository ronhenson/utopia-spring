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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

  @BeforeEach
  void setAirportDaoOutput() {
    when(airportDao.findById(NOT_FOUND)).thenReturn(Optional.empty());
    when(airportDao.findById(IATALIDENT)).thenReturn(Optional.of(AIRPORT));
    when(airportDao.save(AIRPORT)).thenReturn(AIRPORT);
    when(airportDao.existsById(AIRPORT.getIataIdent())).thenReturn(true);
    when(airportDao.existsById(AIRPORT_NOT_FOUND.getIataIdent())).thenReturn(false);
    when(airportDao.search(QUERY)).thenReturn(List.of(AIRPORT));
    when(airportDao.findAll()).thenReturn(List.of(AIRPORT));
  }

  @Test
  @DisplayName("get airport by iatalIdent, not found, expect False")
  void test1() {
    assertFalse(airportService.getAirportById(NOT_FOUND).isPresent());
  }

  @Test
  @DisplayName("get airport by iatalIdent, expect True")
  void test2() {
    Optional<Airport> airport = airportService.getAirportById(IATALIDENT);
    assertTrue(airport.isPresent());
  }

 @Test
 @DisplayName("update airport, expect Exception")
 void test3() {
   assertThrows(ResourceDoesNotExistsException.class, () -> {
     airportService.updateAirport(AIRPORT_NOT_FOUND);
   });
 }

 @Test
 @DisplayName("update airport, expect True")
 void test4() throws ResourceDoesNotExistsException {
     assertThat(AIRPORT, samePropertyValuesAs(airportService.updateAirport(AIRPORT)));
 }

 @Test
 @DisplayName("delete airport by Id, expect exception ")
 void test5() {
   assertThrows(ResourceDoesNotExistsException.class, () -> {
     airportService.deleteAirport(IATALIDENTDEL);
   });
 }

 @Test
 @DisplayName("query by airport id and/or name , expect list")
 void test6() {
     List<Airport> airportList = airportService.search(QUERY);
     assertThat(airportList.get(0), samePropertyValuesAs(AIRPORT));
 }

 @Test
 @DisplayName("Find all, expect list")
 void test7() throws ResourceDoesNotExistsException {
    List<Airport> airportList = airportService.getAllAirports();
    assertThat(airportList.get(0), samePropertyValuesAs(AIRPORT));
 }
}
