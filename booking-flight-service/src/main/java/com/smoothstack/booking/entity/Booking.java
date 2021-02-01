package com.smoothstack.booking.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_booking")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	@NonNull
	private Boolean isActive;

	@NonNull
	private String stripeId;

	private Integer bookerId;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "tbl_flight_has_bookings", joinColumns = {@JoinColumn(name="bookingId")}, inverseJoinColumns = {@JoinColumn(name="flightId", insertable = false, updatable = false)})
	private List<Flight> flights;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "tbl_bookings_has_travelers", joinColumns = {@JoinColumn(name="bookingId")}, inverseJoinColumns = {@JoinColumn(name="travelerId")})
	private Set<Traveler> travelers;
}
