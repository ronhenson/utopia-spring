package com.smoothstack.booking;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.smoothstack.booking.dao.FlightDao;
import com.smoothstack.booking.dao.FlightDetailsDao;
import com.smoothstack.booking.entity.Flight;
import com.smoothstack.booking.entity.FlightDetails;
import com.smoothstack.booking.entity.MultiHopFlight;
import com.smoothstack.booking.service.FlightDetailsService;
import com.smoothstack.booking.service.FlightService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class FlightServiceTests {

  @Mock
  private FlightDao flightDao;

  @Mock
  private FlightDetailsDao flightDetailsDao;

  @InjectMocks
  private FlightService flightService;

  @Mock
  FlightDetailsService flightDetailsService;

  private final long FLIGHT_ID = 3l;
  private final long FLIGHT_ID_TWO = 4l;
  private final Long NOT_FOUND_FLIGHT_ID = 0L;

  private final String FLIGHT_ONE_DATE = "2021-10-15";
  private final String FLIGHT_TWO_DATE = "2021-10-15";

  private final LocalDateTime FLIGHT_ONE_START = getFormattedDate(FLIGHT_ONE_DATE, true);
  private final LocalDateTime FLIGHT_ONE_END = getFormattedDate(FLIGHT_ONE_DATE, false);
  private final LocalDateTime FLIGHT_TWO_START = getFormattedDate(FLIGHT_TWO_DATE, true);
  private final LocalDateTime FLIGHT_TWO_END = getFormattedDate(FLIGHT_TWO_DATE, false);
  private final LocalDateTime FLIGHT_ONE_ACTUAL_START = getFormattedDate("2021-10-15 08:00:00");
  private final LocalDateTime FLIGHT_ONE_ACTUAL_END = getFormattedDate("2021-10-15 12:00:00");
  private final LocalDateTime FLIGHT_TWO_ACTUAL_START = getFormattedDate("2021-10-15 13:00:00");
  private final LocalDateTime FLIGHT_TWO_ACTUAL_END = getFormattedDate("2021-10-15 16:00:00");

  private final List<String> FLIGHT_NUMBERS = List.of("12345");
  private final List<String> FLIGHT_NUMBERS_TWO = List.of("67890");
  private final List<String> HUBS = Arrays.asList("HUB");
  private final List<String> NOT_FOUND_HUBS = Arrays.asList("HUB");
  private final String NOT_FOUND = "NFO";
  private final List<String> NOT_FOUND_FLIGHT_NUMBERS = List.of("00000");
  private final String ORIGIN = "ORI";
  private final String DEST = "DES";
  private final String HUB = "HUB";


  private final FlightDetails FLIGHT_DETAILS = new FlightDetails(
    FLIGHT_NUMBERS.get(0),
    ORIGIN, // departcityid
    DEST // arrivecityid
  );

  private final FlightDetails FLIGHT_DETAILS_ONE = new FlightDetails(
    FLIGHT_NUMBERS.get(0),
    ORIGIN, // departcityid
    HUB // arrivecityid
  );
  private final FlightDetails FLIGHT_DETAILS_TWO = new FlightDetails(
    FLIGHT_NUMBERS_TWO.get(0),
    HUB, // departcityid
    DEST // arrivecityid
  );

  private final Flight FLIGHT = new Flight(
    FLIGHT_ID,
    FLIGHT_ONE_ACTUAL_START,
    FLIGHT_ONE_ACTUAL_END,
    20,     // vacant seats
    350.00, // price
    FLIGHT_NUMBERS.get(0),
    FLIGHT_DETAILS);

  private final Flight FLIGHT_ONE = new Flight(
    FLIGHT_ID,
    FLIGHT_ONE_ACTUAL_START,
    FLIGHT_ONE_ACTUAL_END,
    20,     // vacant seats
    350.00, // price
    FLIGHT_NUMBERS.get(0),
    FLIGHT_DETAILS_ONE);

  private final Flight FLIGHT_TWO = new Flight(
    FLIGHT_ID_TWO,
    FLIGHT_TWO_ACTUAL_START,
    FLIGHT_TWO_ACTUAL_END,
    40,     // vacant seats
    450.00, // price
    FLIGHT_NUMBERS_TWO.get(0),
    FLIGHT_DETAILS_TWO);

  private final MultiHopFlight multiHopFlight = new MultiHopFlight (
    FLIGHT_ONE,
    FLIGHT_TWO
  );

  MultiHopFlight flights = new MultiHopFlight();


  @BeforeEach
  void setFlightDetailsDaoOutput() {
    when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(ORIGIN, HUBS)).thenReturn(List.of(FLIGHT_DETAILS));
    // when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(NOT_FOUND, HUBS)).thenReturn(List.of());
    // when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(ORIGIN, NOT_FOUND_HUBS)).thenReturn(List.of());

    when(flightDetailsDao.findByArriveCityIdAndDepartCityIdIn(DEST, HUBS)).thenReturn(List.of(FLIGHT_DETAILS_TWO));
    // when(flightDetailsDao.findByArriveCityIdAndDepartCityIdIn(NOT_FOUND, HUBS)).thenReturn(List.of());

    when(flightDetailsDao.findByArriveCityIdAndDepartCityId(DEST, ORIGIN)).thenReturn(List.of(FLIGHT_DETAILS));
    // when(flightDetailsDao.findByArriveCityIdAndDepartCityId(DEST, NOT_FOUND`)).thenReturn(List.of());
    // when(flightDetailsDao.findByArriveCityIdAndDepartCityId(NOT_FOUND, ORIGIN)).thenReturn(List.of());
  }

  @Test
  @DisplayName("Direct non stop flight, find flights, expect flight list")
  void findFlights() {
    when(flightDetailsService.findByOriginDest(ORIGIN, DEST)).thenReturn(List.of(FLIGHT_DETAILS));
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(FLIGHT_NUMBERS, FLIGHT_ONE_START, FLIGHT_ONE_END))
        .thenReturn(List.of(FLIGHT));

    List<Flight> flights = flightService.findFlights(ORIGIN, DEST, FLIGHT_ONE_DATE);
    assertThat(flights, is(List.of(FLIGHT)));

  }

  @Test
  @DisplayName("Direct Non Stop Flight, find flights, expect flight empty list")
  void findFlightsNotFound() {
    when(flightDetailsService.findByOriginDest(ORIGIN, DEST)).thenReturn(List.of(FLIGHT_DETAILS));
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(List.of(), FLIGHT_ONE_START, FLIGHT_ONE_END))
      .thenReturn(List.of());

    List<Flight> flights = flightService.findFlights(NOT_FOUND, DEST, FLIGHT_ONE_DATE);
    assertThat(flights, is(List.of()));
  }

  //TODO round trip test

  @Test
  @DisplayName("MultiHop Flight, find flights, expect flight Leg1 and Leg2 list")
  void findMultiHopFlight() {
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(FLIGHT_NUMBERS, FLIGHT_ONE_START, FLIGHT_ONE_END))
      .thenReturn(List.of(FLIGHT_ONE));
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(FLIGHT_NUMBERS_TWO, FLIGHT_TWO_START, FLIGHT_TWO_END))
      .thenReturn(List.of(FLIGHT_TWO));
    when(flightDetailsService.findHubsFromOrigin(ORIGIN)).thenReturn(List.of(FLIGHT_DETAILS_ONE));
    when(flightDetailsService.findHubsFromDest(DEST)).thenReturn(List.of(FLIGHT_DETAILS_TWO));

    System.out.println("Origin " + ORIGIN + "  dest " + DEST + "  Start date " + FLIGHT_ONE_DATE);
    List<MultiHopFlight> flights = flightService.findByMultiHop(ORIGIN, DEST, FLIGHT_ONE_DATE);
    List<Flight> leg1 = List.of(flights.get(0).getLeg1());
    List<Flight> leg2 = List.of(flights.get(0).getLeg2());
    System.out.println(leg1.get(0).getFlightId() + "  =  " + FLIGHT_ONE.getFlightId());
    System.out.println(leg2.get(0).getFlightId() + "  =  " + FLIGHT_TWO.getFlightId());
    assertEquals(FLIGHT_ONE.getFlightId(), leg1.get(0).getFlightId());
    assertEquals(FLIGHT_TWO.getFlightId(), leg2.get(0).getFlightId());
  }

  //TODO No connecting flights within time parameters

  //TODO Round Trip with multi-hop

  private LocalDateTime getFormattedDate(String date, boolean isStartDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateString = isStartDate ? date + " 00:00:00" : date + " 23:59:59";
    return LocalDateTime.parse(dateString, formatter);
  }

  private LocalDateTime getFormattedDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(date, formatter);
  }

}
