package com.smoothstack.airlines.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.smoothstack.airlines.entity.primaryKeys.BookingsHasTravelersKey;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@IdClass(BookingsHasTravelersKey.class)
@Table(name = "tbl_bookings_has_travelers")
public class BookingsHasTravelers {
	
	@NonNull
	@Id
	@Column(name = "bookings_bookingId")
	private Integer bookingsBookingId;
	
	@NonNull
	@Id
	@Column(name = "bookings_flightId")
	private Integer bookingsFlightId;
	
	@NonNull
	@Id
	@Column(name = "traveler_travelerId")
	private Integer travelerTravelerId;
}
