package com.application.medievalwarefare.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ExceptionAdvice {
	@ExceptionHandler(InsufficientWinsException.class)
	public ResponseEntity<ExceptionDetail> noHandlerFound(InsufficientWinsException ex) {
		ExceptionDetail error = new ExceptionDetail(ex.getStatus(), ex.getMessage());

		return this.buildResponseEntity(error);
	}
}
