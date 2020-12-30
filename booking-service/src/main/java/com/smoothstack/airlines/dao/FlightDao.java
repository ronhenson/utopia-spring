package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.airlines.entity.Flight;
import com.smoothstack.airlines.entity.primaryKeys.FlightKey;

@Repository
public interface FlightDao extends JpaRepository<Flight, FlightKey> {
	Flight findByFlightId(Integer flightId);
}
