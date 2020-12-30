package com.smoothstack.airlines.entity;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

	@NonNull
	private Boolean isActive;

	@NonNull
	private String stripeId;

	@NonNull
	private Integer bookerId;

	private ArrayList<Integer> travelerIds = new ArrayList<>();

	public Booking getBooking() {
		return new Booking(isActive, stripeId, bookerId);
	}
}

