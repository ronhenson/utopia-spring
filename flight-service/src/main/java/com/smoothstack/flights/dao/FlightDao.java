package com.smoothstack.flights.dao;

import com.smoothstack.flights.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlightDao extends JpaRepository<Flight, Long> {

}
