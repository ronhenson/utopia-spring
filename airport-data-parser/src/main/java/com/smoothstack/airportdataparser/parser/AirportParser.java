package com.smoothstack.airportdataparser.parser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class AirportParser implements CommandLineRunner {

    private void parse(File file) {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    @Override
    public void run(String... args) throws Exception {
        try {
            File file = ResourceUtils.getFile("classpath:flight-data/L_AIRPORT.csv");
            System.out.println(file);
        } catch (FileNotFoundException e) {
            System.err.println("no flight data in resources directory");
            System.err.println(e);
        }
    }
}
