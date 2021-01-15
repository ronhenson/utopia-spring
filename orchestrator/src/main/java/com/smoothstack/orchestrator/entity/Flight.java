package com.smoothstack.orchestrator.entity;

import lombok.*;
import javax.persistence.*;
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
    private String flightNumber;

    @ManyToOne()
    @JoinColumn(name="flightNumber", referencedColumnName = "flightNumber", insertable = false, updatable = false)
    private FlightDetails flightDetails;

}
