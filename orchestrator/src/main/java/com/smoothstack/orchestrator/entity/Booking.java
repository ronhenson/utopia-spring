package com.smoothstack.orchestrator.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private Long bookingId;

	@NonNull
	private Boolean isActive;

	@NonNull
	private String stripeId;

	private Integer bookerId;

	private List<Flight> flights;

	private Set<Traveler> travelers = new HashSet<>();

}
