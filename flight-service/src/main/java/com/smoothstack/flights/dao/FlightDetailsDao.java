package com.smoothstack.flights.dao;

import com.smoothstack.flights.entity.Flight;
import com.smoothstack.flights.entity.FlightDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDetailsDao extends JpaRepository<FlightDetails, Long> {

    List<FlightDetails> findByDepartCityIdAndArriveCityIdIn(String departId, List<String> hubs);
    List<FlightDetails> findByArriveCityIdAndDepartCityIdIn(String arriveId, List<String> hubs);
    List<FlightDetails>findByArriveCityIdAndDepartCityId(String arriveId, String departCityId);

}
