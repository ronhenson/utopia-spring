package com.smoothstack.com.flightdataparser.parser;
import com.smoothstack.com.flightdataparser.parser.entity.Flight;
import com.smoothstack.com.flightdataparser.parser.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FlightParser implements CommandLineRunner {
    @Autowired
    FlightService flightService;

    private String cleanQuotes(String value) {
        return value.substring(1, value.length() -1);
    }

    private LocalDateTime parseDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter).plusYears(2);
        return dateTime;
    }

    private Flight parseRow(String[] row) {
        Flight flight = new Flight();
        double price = Double.parseDouble(row[13])*.135;
        flight.setArrivalTime(parseDateTime(row[4] + " " + cleanQuotes(row[11])));
        flight.setDepartTime(parseDateTime(row[4] + " " + cleanQuotes(row[10])));
        flight.setPrice(price > 50 ? price : 50);
        flight.setSeatsAvailable(150);
        flight.setDepartCityId(cleanQuotes(row[8]));
        flight.setArriveCityId(cleanQuotes(row[9]));
        flight.setFlightNumber(cleanQuotes(row[7]));
        return flight;
    }

    private void parse(File file) {

        try(BufferedReader br = new BufferedReader(new FileReader(file), 1048576)) {
            System.out.println(br.readLine());
            ArrayList<Flight> flightList = new ArrayList<>();
            String line;
            Flight flight;
            while((line = br.readLine()) != null) {
                flight = parseRow(line.split(","));
                flightList.add(flight);
                //System.out.println(flight.getDepartTime().toString());
            }
            flightService.saveAllFlights(flightList);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        File file;
        try {
            file = ResourceUtils.getFile("classpath:flight-data/ONTIME_REPORTING_CLEAN.csv");
            System.out.println(file);
            parse(file);
        } catch (FileNotFoundException e) {
            System.out.println("no flight data in resources directory");
        }

    }
}
