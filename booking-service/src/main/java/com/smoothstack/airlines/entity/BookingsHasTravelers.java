package com.smoothstack.airlines.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_bookings_has_travelers")
@IdClass(BookingTravelerKey.class)
public class BookingsHasTravelers implements Serializable {

	private static final long serialVersionUID = 468776497453268434L;


	@NonNull
	@Id
	private Integer bookingId;
	
	
	@NonNull
	@Id
	private Integer travelerId;
}
