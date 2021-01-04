package com.smoothstack.com.flightdataparser.parser.service;

import com.smoothstack.com.flightdataparser.parser.dao.FlightDao;
import com.smoothstack.com.flightdataparser.parser.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
    @Autowired
    FlightDao flightDao;

    public Flight createFlight(Flight flight) {
        return flightDao.save(flight);
    }
}
