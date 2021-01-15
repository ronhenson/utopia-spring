package com.smoothstack.airportdataparser.dao;

import com.smoothstack.airportdataparser.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportDao extends JpaRepository<Airport, String> {
}
