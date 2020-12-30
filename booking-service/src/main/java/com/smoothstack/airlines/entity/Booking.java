package com.smoothstack.airlines.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smoothstack.airlines.entity.primaryKeys.BookingKey;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_booking")
@IdClass(BookingKey.class)
public class Booking {

	@NonNull @Id private Integer bookingId;
	
	@NonNull @Id private Integer flightId;

	@NonNull private Boolean isActive;

	@NonNull private String stripeId;

	@NonNull private Integer bookerId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "tbl_flight_has_bookings", joinColumns = {
			@JoinColumn(name = "bookings_bookingId", referencedColumnName = "bookingId"),
			@JoinColumn(name = "bookings_flightId", referencedColumnName = "flightId") }, inverseJoinColumns = {
					@JoinColumn(name = "flights_flightId", referencedColumnName = "flightId"),
					@JoinColumn(name = "flights_departTime", referencedColumnName = "departTime"),
					@JoinColumn(name = "flights_departCityId", referencedColumnName = "departCityId"),
					@JoinColumn(name = "flights_arriveCityId", referencedColumnName = "arriveCityId") })
	private Flight flight;

	@Builder.Default
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "tbl_bookings_has_travelers", joinColumns = {
			@JoinColumn(name = "bookings_bookingId", referencedColumnName = "bookingId"),
			@JoinColumn(name = "bookings_flightId", referencedColumnName = "flightId") }, inverseJoinColumns = {
					@JoinColumn(name = "traveler_travelerId", referencedColumnName = "travelerId") })
	private Set<Traveler> travelers = new HashSet<>();
}
