package com.smoothstack.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.smoothstack.booking.dao.FlightDao;
import com.smoothstack.booking.entity.Flight;
import com.smoothstack.booking.entity.FlightDetails;
import com.smoothstack.booking.entity.MultiHopFlight;

@Service
public class FlightService {
    @Autowired
    FlightDao flightDao;

    @Autowired
    FlightDetailsService flightDetailsService;

    private LocalDateTime getFormattedDate(String date, boolean isStartDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = isStartDate ? date + " 00:00:00" : date + " 23:59:59";
        return LocalDateTime.parse(dateString, formatter);
    }


    private List<MultiHopFlight> matchConnectingFlights(List<Flight> originFlts, List<Flight> terminatingFlts) {
        List<MultiHopFlight> resultList = new ArrayList<>();
        for(int i = 0; i < originFlts.size(); ++i) {
            for(int j = 0; j < terminatingFlts.size(); ++j) {
                FlightDetails originDet = originFlts.get(i).getFlightDetails();
                FlightDetails destDet = terminatingFlts.get(j).getFlightDetails();
                Flight origin = originFlts.get(i);
                Flight dest = terminatingFlts.get(j);
                long mins = LocalDateTime.from(dest.getDepartTime()).until(origin.getArrivalTime(), ChronoUnit.MINUTES);
                if (originDet.getArriveCityId().equals(destDet.getDepartCityId()) && dest.getDepartTime().isAfter(origin.getArrivalTime()) && mins > -180) {
                    resultList.add(new MultiHopFlight(origin, dest));
                    System.out.println(resultList.get(resultList.size() - 1));
                }
            }
        }
        return resultList;
    }

    public List<MultiHopFlight> findByMultiHop(String origin, String dest, String date) {
        List<FlightDetails> originToHubFlts = flightDetailsService.findHubsFromOrigin(origin); // get a list of flights from origin to a hub airport;
        List<FlightDetails> destToHubFlts = flightDetailsService.findHubsFromDest(dest); // get a list of flights from hub to destination
        LocalDateTime start = getFormattedDate(date, true); //takes departure date string and returns date time for beginning of departure day.
        LocalDateTime end = getFormattedDate(date, false); // takes departure date string and returns date time for end of departure day.
        List<String> leg1FltNums = originToHubFlts.parallelStream().map(flt -> flt.getFlightNumber()).collect(Collectors.toList()); // maps flt numbers from flight details object to string list of flight numbers for origin to hub flts
        List<String> leg2FltNums = destToHubFlts.parallelStream().map(flt -> flt.getFlightNumber()).collect(Collectors.toList());  // map for hub to dest flights
        List<Flight> leg1 = flightDao.findByFlightNumberInAndDepartTimeBetween(leg1FltNums, start, end); // gets list of Flight objs from list of origin to hub flights;
        List<Flight> leg2 = flightDao.findByFlightNumberInAndDepartTimeBetween(leg2FltNums, start, end); // get list of Flight objs using list of hub to dest flights;
        return matchConnectingFlights(leg1, leg2); //iterates thru leg1 and leg2 and creates list of MultiHop objs with flights that much the required connection criteria;
    }

    //direct non stop flights
    public List<Flight> findFlights(String origin, String dest, String date) {
        LocalDateTime start = getFormattedDate(date, true); //takes departure date string and returns date time for beginning of departure day.
        LocalDateTime end = getFormattedDate(date, false); // takes departure date string and returns date time for end of departure day.
        List<FlightDetails> flightDetails = flightDetailsService.findByOriginDest(origin, dest);
        List<String> fltNums = flightDetails.parallelStream().map(fltD -> fltD.getFlightNumber()).collect(Collectors.toList());
        return flightDao.findByFlightNumberInAndDepartTimeBetween(fltNums, start, end);
    }

    public Optional<Flight> findByFlightId(Long id) {
        return flightDao.findById(id);
    }


}
