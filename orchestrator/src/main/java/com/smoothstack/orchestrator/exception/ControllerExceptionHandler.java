package com.smoothstack.orchestrator.exception;

import org.springframework.http.HttpStatus;
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

	@ExceptionHandler(EmailNotFoundException.class)
	protected ResponseEntity<Object> handleConflict(EmailNotFoundException ex) {
		return handleExceptionInternal(ex, "Email address `%s` not found.".formatted(ex.getEmail()), null,
				HttpStatus.BAD_REQUEST, null);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	protected ResponseEntity<Object> handleConflict(InvalidPasswordException ex) {
		return handleExceptionInternal(ex, "Invalid password", null, HttpStatus.BAD_REQUEST, null);
	}
}
