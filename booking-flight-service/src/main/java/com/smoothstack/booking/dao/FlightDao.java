package com.smoothstack.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.smoothstack.booking.entity.Flight;

@Repository
public interface FlightDao extends JpaRepository<Flight, Long> {
    List<Flight> findByFlightNumberInAndDepartTimeBetween(List<String> flightNums, LocalDateTime start, LocalDateTime end);
}
