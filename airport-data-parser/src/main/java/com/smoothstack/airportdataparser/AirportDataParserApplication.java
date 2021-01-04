package com.smoothstack.airportdataparser;

import com.smoothstack.airportdataparser.parser.AirportParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AirportDataParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportDataParserApplication.class, args);
    }

    @Bean
    AirportParser startParser() {
        return new AirportParser();
    }
}
