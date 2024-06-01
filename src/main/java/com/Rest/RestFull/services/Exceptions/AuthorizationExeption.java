package com.Rest.RestFull.services.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationExeption extends AccessDeniedException {

	public AuthorizationExeption(String message) {
		super(message);
	}
}