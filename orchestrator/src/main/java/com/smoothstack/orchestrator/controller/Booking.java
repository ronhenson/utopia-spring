package com.smoothstack.orchestrator.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Booking {

	private Integer bookingId;

	@NonNull
	private Boolean isActive;

	@NonNull
	private String stripeId;

	@NonNull
	private Integer bookerId;

}
