package com.smoothstack.airportdataparser.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table( name = "tbl_airport")
public class Airport {
    @Id
    private String iataIdent;
    @NonNull
    private String city;
    @NonNull
    private String name;
}
