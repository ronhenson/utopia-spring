package com.smoothstack.constants;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
	TRAVELER("traveler"), BOOKING("booking"), FLIGHT("flight");

	@NonNull private String text;
}