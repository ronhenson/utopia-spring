package com.smoothstack.airportdataparser.parser;

import com.opencsv.exceptions.CsvException;
import com.smoothstack.airportdataparser.entity.Airport;
import com.smoothstack.airportdataparser.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.ResourceUtils;
import com.opencsv.CSVReader;
import java.io.*;
import java.util.Arrays;

public class AirportParser implements CommandLineRunner {

    private static final byte QUOTE = 34;
    @Autowired
    AirportService airportService;
    private String cleanQuotes(String value) {
        return value.substring(1, value.length() -1);
    }

    private void parse(File file) {
        try(CSVReader reader = new CSVReader(new FileReader(file))) {
            //System.out.println(br.readLine());
            reader.readNext();
            reader.readAll().forEach(row -> {
                String[] cityName = row[1].split(":");
                Airport airport = new Airport();
                airport.setIataIdent(row[0]);
                airport.setCity(cityName[0]);
                airport.setName(cityName[1]);
                airportService.createAirport(airport);
            });
        } catch (IOException | CsvException e) {
            System.err.println(e);
        }
    }
    @Override
    public void run(String... args) throws Exception {
        try {
            File file = ResourceUtils.getFile("classpath:flight-data/L_AIRPORT.csv");
            System.out.println(file);
            parse(file);
        } catch (FileNotFoundException e) {
            System.err.println("no flight data in resources directory");
            System.err.println(e);
        }
    }
}
