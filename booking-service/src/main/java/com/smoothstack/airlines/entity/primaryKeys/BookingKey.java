package com.smoothstack.airlines.entity.primaryKeys;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BookingKey implements Serializable {
	
	private static final long serialVersionUID = -22589358761022863L;

	@NonNull private Integer bookingId;
	
	@NonNull private Integer flightId;
	
}
