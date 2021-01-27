package com.smoothstack.orchestrator.entity;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Traveler {

  private Integer travelerId;
	
	@NonNull private String name;
	
	@NonNull private String address;
	
	@NonNull private String phone;
	
	@NonNull private String email;
	
	@NonNull private Timestamp dob;
}
