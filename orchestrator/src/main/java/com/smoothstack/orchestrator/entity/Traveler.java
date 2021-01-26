package com.smoothstack.orchestrator.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Traveler {

  @NonNull private Integer travelerId;
	
	@NonNull private String name;
	
	@NonNull private String address;
	
	@NonNull private String phone;
	
	@NonNull private String email;
	
	@NonNull private Timestamp dob;
}
