package com.smoothstack.com.flightdataparser.parser;
import org.springframework.boot.CommandLineRunner;

public class FlightParser implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("hello: " + args[0]);
    }
}
