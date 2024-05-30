package com.Rest.RestFull.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	private final int status;
	private final String message;
	private String stackTrace;
	private List<ValidationError> errors;
	
	@Getter
	@Setter
	@RequiredArgsConstructor
	
	private static class ValidationError{
		private final String field;
		private final String message;
		
		
	}
	
	public void addValidationError(String field, String message){
		if (Objects.isNull(errors)) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(new ValidationError(field, message));
	}

	public ErrorResponse(int status, String message, String stackTrace, List<ValidationError> errors) {
		super();
		this.status = status;
		this.message = message;
		this.stackTrace = stackTrace;
		this.errors = errors;
	}

	public String toJson() {
		return "(\"status\": " + getStatus() + ", " + 
				"\"message\": \"" + getMessage() + "\")";
	}
	

	
	
	
}
