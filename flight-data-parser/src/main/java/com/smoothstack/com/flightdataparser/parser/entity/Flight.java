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
    private LocalDateTime arrivalTime;

    @NonNull
    private String flightNumber;

}
