package com.application.medievalwarefare.exception;

import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

public abstract class ExceptionAdvice {
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ExceptionDetail> noHandlerFound(NoHandlerFoundException ex) {
		ExceptionDetail error = new ExceptionDetail(ex.getStatusCode(), ex.getMessage());

		return this.buildResponseEntity(error, ex.getHeaders());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionDetail> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		ExceptionDetail error = new ExceptionDetail(ex.getStatusCode(), ex.getMessage());

		return this.buildResponseEntity(error, ex.getHeaders());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ExceptionDetail> httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		ExceptionDetail error = new ExceptionDetail(ex.getStatusCode(), ex.getMessage());

		return this.buildResponseEntity(error, ex.getHeaders());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionDetail> methodArgumentNotValid(MethodArgumentNotValidException ex) {
		ExceptionDetail error = new ExceptionDetail(ex.getStatusCode(), "Validation error");

		error.addFieldErrors(ex.getBindingResult().getFieldErrors());

		error.addObjectErrors(ex.getBindingResult().getGlobalErrors());

		return this.buildResponseEntity(error, ex.getHeaders());
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ExceptionDetail> invalidFormat(InvalidFormatException ex) {
		ExceptionDetail error = new ExceptionDetail(HttpStatus.BAD_REQUEST, "JSON mapping error");

		String message = "must be of type " + ex.getTargetType().getSimpleName();

		String rejectedValue = ex.getValue().toString();

		String field = ex.getPath().size() > 0 ? ex.getPath().get(0).getFieldName() : null;

		error.addValidationError(field, message, rejectedValue);

		return this.buildResponseEntity(error);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionDetail> httpMessageNotReadable(HttpMessageNotReadableException ex) {

		if (ex.getCause() instanceof InvalidFormatException subEx) return this.invalidFormat(subEx);

		ExceptionDetail error = new ExceptionDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request");

		return this.buildResponseEntity(error);
	}

	@ExceptionHandler(HttpMessageNotWritableException.class)
	public ResponseEntity<ExceptionDetail> httpMessageNotWritable(HttpMessageNotWritableException ex) {
		ExceptionDetail error = new ExceptionDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing JSON output");

		return this.buildResponseEntity(error);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ExceptionDetail> noSuchElement(NoSuchElementException ex) {
		ExceptionDetail error = new ExceptionDetail(HttpStatus.NOT_FOUND, ex.getMessage());

		return this.buildResponseEntity(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDetail> exception(Exception ex) {
		ExceptionDetail error = new ExceptionDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);

		return this.buildResponseEntity(error);
	}

	public ResponseEntity<ExceptionDetail> buildResponseEntity(ExceptionDetail error, HttpHeaders headers) {
		return ResponseEntity.status(error.getStatus())
				.headers(headers)
				.contentType(MediaType.APPLICATION_JSON)
				.body(error);
	}

	public ResponseEntity<ExceptionDetail> buildResponseEntity(ExceptionDetail error) {
		return ResponseEntity.status(error.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(error);
	}
}
