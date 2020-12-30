package com.smoothstack.airlines.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_flight")
public class Flight {

	@NonNull
	@Id
	private Integer flightId;

	@NonNull
	private Timestamp departTime;

	@NonNull
	private String departCityId;

	@NonNull
	private String arriveCityId;

	@NonNull
	private Integer seatsAvailable;
	
	@NonNull
	private Float price;
	
	@NonNull
	private Timestamp arrivalTime;
	
	@NonNull
	private String flightNumber;

	@JsonIgnore
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Booking> bookings;
}
