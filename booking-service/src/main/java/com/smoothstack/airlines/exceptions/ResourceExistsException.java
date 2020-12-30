package com.smoothstack.airlines.exceptions;

import com.smoothstack.constants.ResourceType;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ResourceExistsException extends Exception {
	
	private static final long serialVersionUID = -1336797486884051669L;
	
	private

	@NonNull Integer resourceId;
	
	@NonNull ResourceType resourceType;
	
	@Override
	public String getMessage() {
		return "The resource `%s` of id `%d` already exists.".formatted(resourceType.getText(), resourceId);
	}

}
