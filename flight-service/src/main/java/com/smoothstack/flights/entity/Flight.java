package com.smoothstack.flights.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_flight")
public class Flight {
    @Id
    private long flightId;

    @NonNull
    private LocalDateTime departTime;

    @NonNull
    private LocalDateTime arrivalTime;

    @NonNull
    private int seatsAvailable;

    @NonNull
    private double price;

    @NonNull
    String departCityId;

    @NonNull
    private String arriveCityId;

    @NonNull
    private String flightNumber;

}
