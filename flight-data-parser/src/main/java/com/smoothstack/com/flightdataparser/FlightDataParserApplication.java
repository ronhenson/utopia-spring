package com.smoothstack.com.flightdataparser;

import com.smoothstack.com.flightdataparser.parser.FlightParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FlightDataParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightDataParserApplication.class, args);
    }

    @Bean
    public FlightParser startFlightParser() {
        return new FlightParser();
    }

}
