package com.smoothstack.airlines.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private List<Integer> travelerIds = new ArrayList<>();

	@JsonIgnore
	public Booking getBooking() {
		return new Booking(isActive, stripeId);
	}
	
	public BookingRequest(Booking booking) {
		isActive = booking.getIsActive();
		stripeId = booking.getStripeId();
	}
	
	public BookingRequest(Booking booking, List<Integer> travelerIds) {
		isActive = booking.getIsActive();
		stripeId = booking.getStripeId();
		this.travelerIds = travelerIds;
	}
}

