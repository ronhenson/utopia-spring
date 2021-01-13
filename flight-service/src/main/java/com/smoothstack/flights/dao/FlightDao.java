package com.smoothstack.flights.dao;

import com.smoothstack.flights.entity.Flight;
import com.smoothstack.flights.entity.FlightDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight, Long> {

    List<Flight> findByFlightNumberInAndDepartTimeBetween(List<String> flightNums, LocalDateTime start, LocalDateTime end);
}
