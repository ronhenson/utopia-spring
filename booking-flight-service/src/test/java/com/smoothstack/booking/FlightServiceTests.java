package com.smoothstack.booking;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.smoothstack.booking.dao.FlightDao;
import com.smoothstack.booking.dao.FlightDetailsDao;
import com.smoothstack.booking.entity.Flight;
import com.smoothstack.booking.entity.FlightDetails;
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

  private final String START_DATE = "2021-10-15";
  private final String END_DATE = "2021-11-01";
  // private final String START_DATE_HOP = "2021-10-15";
  // private final String END_DATE_HOP = "2021-11-01";

  private final LocalDateTime START_DATE_TIME = getFormattedDate(START_DATE, true);
  private final LocalDateTime END_DATE_TIME = getFormattedDate(END_DATE, false);
  // private final LocalDateTime START_DATE_HOP_TIME = getFormattedDate(START_DATE_HOP, true);
  // private final LocalDateTime END_DATE_HOP_TIME = getFormattedDate(END_DATE_HOP, false);

  private final List<String> FLIGHT_NUMBERS = List.of("12345");
  private final List<String> HUBS = Arrays.asList("dfw");
  private final List<String> NOT_FOUND_LIST = List.of();
  private final String NOT_FOUND = "";
  private final String ORIGIN = "LAX";
  private final String DEST = "DFW";


  @BeforeEach
  void init() {
    this.flightDetailsService = new FlightDetailsService();
}
  private final FlightDetails FLIGHT_DETAILS = new FlightDetails(
    FLIGHT_NUMBERS.get(0),
    ORIGIN, // departcityid
    DEST // arrivecityid
  );

  private final Flight FLIGHT = new Flight(FLIGHT_ID, START_DATE_TIME, END_DATE_TIME, 20, // vacant seats
      350.00, // price
      FLIGHT_NUMBERS.get(0), FLIGHT_DETAILS);

@BeforeEach
void setFlightDetailsService() {
  when(flightDetailsService.findByOriginDest(ORIGIN, DEST)).thenReturn(List.of(FLIGHT_DETAILS));
}

  @BeforeEach
  void setflightDaoOutput() {
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(FLIGHT_NUMBERS, START_DATE_TIME, END_DATE_TIME)).thenReturn(List.of(FLIGHT));
    when(flightDao.findByFlightNumberInAndDepartTimeBetween(NOT_FOUND_LIST, START_DATE_TIME, END_DATE_TIME))
        .thenReturn(List.of());
  }


  @BeforeEach
  void setFlightDetailsDaoOutput() {
    when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(ORIGIN, HUBS)).thenReturn(List.of(FLIGHT_DETAILS));
    when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(NOT_FOUND, HUBS)).thenReturn(List.of());
    when(flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(ORIGIN, NOT_FOUND_LIST)).thenReturn(List.of());

    when(flightDetailsDao.findByArriveCityIdAndDepartCityIdIn(DEST, HUBS)).thenReturn(List.of(FLIGHT_DETAILS));
    when(flightDetailsDao.findByArriveCityIdAndDepartCityIdIn(NOT_FOUND, HUBS)).thenReturn(List.of());

    when(flightDetailsDao.findByArriveCityIdAndDepartCityId(DEST, ORIGIN)).thenReturn(List.of(FLIGHT_DETAILS));
    when(flightDetailsDao.findByArriveCityIdAndDepartCityId(NOT_FOUND, ORIGIN)).thenReturn(List.of());
  }

  @Test
  @DisplayName("find flights, expect flight list")
  void findFlights() {
    System.out.println("Origin " + ORIGIN + "  dest " + DEST + "  Start date " + START_DATE);
    List<Flight> flights = flightService.findFlights(ORIGIN, DEST, START_DATE);
    assertThat(flights, is(List.of(FLIGHT)));

  }

  private LocalDateTime getFormattedDate(String date, boolean isStartDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateString = isStartDate ? date + " 00:00:00" : date + " 23:59:59";
    return LocalDateTime.parse(dateString, formatter);
  }

}
