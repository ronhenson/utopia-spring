package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.airlines.entity.Flight;

@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {
	Flight findByFlightId(Integer flightId);
}
