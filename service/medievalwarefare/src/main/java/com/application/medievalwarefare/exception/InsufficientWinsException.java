package com.application.medievalwarefare.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InsufficientWinsException extends RuntimeException {
	private final HttpStatus status;
	private final String message;

	public InsufficientWinsException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
}
