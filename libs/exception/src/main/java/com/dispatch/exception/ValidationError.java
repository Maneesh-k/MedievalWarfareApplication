package com.dispatch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationError {
	private String field;
	private String message;
	private Object rejectedValue;

	ValidationError(String message) {
		this.message = message;
	}
}
