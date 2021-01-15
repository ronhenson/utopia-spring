package com.smoothstack.airportdataparser.service;

import com.smoothstack.airportdataparser.dao.AirportDao;
import com.smoothstack.airportdataparser.entity.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {
    @Autowired
    AirportDao airportDao;

    public void createAirport(Airport airport) {
        airportDao.save(airport);
    }
}
