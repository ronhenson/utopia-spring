package com.smoothstack.booking.exceptions;

import com.smoothstack.constants.ResourceType;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 8019158745208127029L;
	
	@NonNull private Integer resourceId;
	
	@NonNull private ResourceType resourceType;
	
	@Override
	public String getMessage() {
		return String.format("The resource `%s` of id `%d` does not exist.", resourceType.getText(), resourceId);
	}
	
}
