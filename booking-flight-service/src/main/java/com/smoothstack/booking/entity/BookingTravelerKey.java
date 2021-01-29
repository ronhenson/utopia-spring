package com.smoothstack.booking.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingTravelerKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6258261161384035757L;

	private Integer bookingId;
	
	private Integer travelerId;
}
