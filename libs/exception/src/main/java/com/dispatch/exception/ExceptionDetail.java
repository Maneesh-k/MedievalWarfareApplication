package com.dispatch.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@Setter
public class ExceptionDetail {
	private HttpStatusCode status;
	private String message;
	private String detail;
	private List<ValidationError> errors;

	public ExceptionDetail(HttpStatusCode status, String message) {
		this.status = status;
		this.message = message;
	}

	public ExceptionDetail(HttpStatusCode status, Throwable ex) {
		this.status = status;
		this.message = "Something went wrong!";
		this.detail = ex.getLocalizedMessage();
	}

	private void addValidationError(ValidationError validationError) {
		if (this.errors == null) this.errors = new ArrayList<>();

		this.errors.add(validationError);
	}

	private void addValidationError(String message) {
		this.addValidationError(new ValidationError(message));
	}

	public void addObjectErrors(List<ObjectError> objectErrors) {
		objectErrors.forEach(objectError -> this.addValidationError(objectError.getDefaultMessage()));
	}

	public void addValidationError(String field, String message, Object rejectedValue) {
		this.addValidationError(new ValidationError(field, message, rejectedValue));
	}

	public void addFieldErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(fieldError -> this.addValidationError(
				fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
	}
}
