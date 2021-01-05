package com.smoothstack.com.flightdataparser.parser.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Flight")
@Table(name = "tbl_flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @NonNull
    LocalDateTime departTime;

    @NonNull
    private int seatsAvailable;

    @NonNull
    private double price;

    @NonNull
    private String departCityId;

    @NonNull
    private String arriveCityId;

    @NonNull
    private LocalDateTime arrivalTime;

    @NonNull
    private String flightNumber;

    @Override
    public String toString() {
        return "flight num: %s\tDeparture: %s\t Arrival: %s\t Departure Time: %s".formatted(flightNumber, departCityId, arriveCityId, departTime.toString());
    }
}
