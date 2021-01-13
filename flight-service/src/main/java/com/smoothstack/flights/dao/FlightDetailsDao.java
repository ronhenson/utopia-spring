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

//    @Query("select tb from FlightDetails tb where tb.departCityId=?1 and tb.arriveCityId in ?2")
    List<FlightDetails> findByDepartCityIdAndArriveCityIdIn(String departId, List<String> hubs);
//    @Query("select tb from FlightDetails tb where tb.arriveCityId=?1 and tb.departCityId in ?2")
    List<FlightDetails> findByArriveCityIdAndDepartCityIdIn(String arriveId, List<String> hubs);

}
