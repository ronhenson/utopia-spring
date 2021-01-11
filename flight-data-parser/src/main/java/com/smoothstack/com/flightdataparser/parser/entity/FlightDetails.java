package com.smoothstack.com.flightdataparser.parser.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @OneToMany(targetEntity = FlightDetails.class, mappedBy = "arriveCityId")
    private List<FlightDetails> flightDetailsList;

}
