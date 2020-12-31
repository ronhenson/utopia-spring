package com.smoothstack.airlines.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

	@NonNull
	private Boolean isActive;

	@NonNull
	private String stripeId;

	@NonNull
	private Integer bookerId;

	private List<Integer> travelerIds = new ArrayList<>();

	public Booking getBooking() {
		return new Booking(isActive, stripeId, bookerId);
	}
	
	public BookingRequest(Booking booking) {
		isActive = booking.getIsActive();
		stripeId = booking.getStripeId();
		bookerId = booking.getBookerId();
	}
	
	public BookingRequest(Booking booking, List<Integer> travelerIds) {
		isActive = booking.getIsActive();
		stripeId = booking.getStripeId();
		bookerId = booking.getBookerId();
		this.travelerIds = travelerIds;
	}
}

