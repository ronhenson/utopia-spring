package com.smoothstack.booking.controller;

import java.net.URISyntaxException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smoothstack.booking.exceptions.DeleteLastTravelerFromBookingException;
import com.smoothstack.booking.exceptions.ResourceExistsException;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceExistsException.class)
	protected ResponseEntity<Object> handleConflict(ResourceExistsException ex) {
		return handleExceptionInternal(ex, ex.getMessage(), null, HttpStatus.CONFLICT, null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleConstraintFailure(DataIntegrityViolationException ex) {
		return handleExceptionInternal(ex, "Foreign key constraint failed.", null, HttpStatus.BAD_REQUEST, null);
	}

	@ExceptionHandler(URISyntaxException.class)
	protected ResponseEntity<Object> handleMalformedURI(URISyntaxException ex) {
		return handleExceptionInternal(ex, "Malformed URI in Location header.", null, HttpStatus.INTERNAL_SERVER_ERROR, null);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
		return handleExceptionInternal(ex, ex.getMessage(), null, HttpStatus.NOT_FOUND, null);
	}
	
	@ExceptionHandler(JsonProcessingException.class)
	protected ResponseEntity<Object> handleBadJson(JsonProcessingException ex) {
		return handleExceptionInternal(ex, ex, null, HttpStatus.BAD_REQUEST, null);
	}

	@ExceptionHandler(DeleteLastTravelerFromBookingException.class)
	protected ResponseEntity<Object> handleBadTravelerDelete(DeleteLastTravelerFromBookingException ex) {
		return handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, null);
	}
}
