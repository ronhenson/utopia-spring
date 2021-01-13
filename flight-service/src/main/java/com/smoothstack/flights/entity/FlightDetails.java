package com.smoothstack.flights.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "FlightDetails")
@Table(name = "tbl_flight_details")
public class FlightDetails {
    @Id
    private String flightNumber;

    @NonNull
    private String departCityId;

    @NonNull
    private String arriveCityId;

}