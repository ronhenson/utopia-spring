package com.smoothstack.airlines.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smoothstack.airlines.entity.primaryKeys.FlightKey;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_flight")
@IdClass(FlightKey.class)
public class Flight {

	@NonNull
	@Id
	private Integer flightId;

	@NonNull
	@Id
	private Timestamp departTime;

	@NonNull
	@Id
	private Integer departCityId;

	@NonNull
	@Id
	private Integer arriveCityId;

	@NonNull
	private Integer seatsAvailable;
	
	@NonNull
	private Float price;

	@JsonIgnore
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Booking> bookings;
}
