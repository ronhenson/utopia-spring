package com.smoothstack.airlines.entity.primaryKeys;

import java.io.Serializable;

import lombok.Data;

@Data
public class BookingsHasTravelersKey implements Serializable {

	private static final long serialVersionUID = 4174396699412867441L;
	
	private Integer bookingsBookingId;
	
	private Integer bookingsFlightId;
	
	private Integer travelerTravelerId;

}
