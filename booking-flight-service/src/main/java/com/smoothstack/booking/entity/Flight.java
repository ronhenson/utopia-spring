package com.smoothstack.booking.entity;

import com.smoothstack.booking.service.FlightService;
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
    private Integer seatsAvailable;

    @NonNull
    private Double price;


    @NonNull
    private String flightNumber;

    @ManyToOne()
    @JoinColumn(name="flightNumber", referencedColumnName = "flightNumber", insertable = false, updatable = false)
    private FlightDetails flightDetails;

}
