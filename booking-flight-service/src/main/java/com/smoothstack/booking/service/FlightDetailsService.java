package com.smoothstack.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.smoothstack.booking.dao.FlightDetailsDao;
import com.smoothstack.booking.entity.FlightDetails;

@Service
public class FlightDetailsService {
    @Autowired
    private FlightDetailsDao flightDetailsDao;

    private final List<String> HUBS = Arrays.asList("PHL", "ORD", "CLT", "MIA", "LAX", "DFW", "LGA", "JFK", "PHX", "DCA");

    public List<FlightDetails> findHubsFromOrigin(String origin) {
        return flightDetailsDao.findByDepartCityIdAndArriveCityIdIn(origin, HUBS);
    }

    public List<FlightDetails> findHubsFromDest(String dest) {
        return flightDetailsDao.findByArriveCityIdAndDepartCityIdIn(dest, HUBS);
    }

    public List<FlightDetails> findByOriginDest(String origin, String dest) {
        return flightDetailsDao.findByArriveCityIdAndDepartCityId(dest, origin);
    }

}
