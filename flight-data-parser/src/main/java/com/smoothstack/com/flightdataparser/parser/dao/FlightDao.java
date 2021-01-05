package com.smoothstack.com.flightdataparser.parser.dao;

import com.smoothstack.com.flightdataparser.parser.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {

}
