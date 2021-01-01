package com.smoothstack.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(HttpClientErrorException.class)
	protected ResponseEntity<Object> handleConflict(HttpClientErrorException ex) {
		return handleExceptionInternal(ex, ex.getResponseBodyAsString(), null, ex.getStatusCode(), null);
	}
}
