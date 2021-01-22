package com.smoothstack.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.smoothstack.booking.entity.FlightDetails;

@Repository
public interface FlightDetailsDao extends JpaRepository<FlightDetails, Long> {

    List<FlightDetails> findByDepartCityIdAndArriveCityIdIn(String departId, List<String> hubs);
    List<FlightDetails> findByArriveCityIdAndDepartCityIdIn(String arriveId, List<String> hubs);
    List<FlightDetails>findByArriveCityIdAndDepartCityId(String arriveId, String departCityId);

}
