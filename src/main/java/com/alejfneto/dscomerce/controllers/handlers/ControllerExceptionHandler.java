package com.alejfneto.dscomerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alejfneto.dscomerce.dto.CustomError;
import com.alejfneto.dscomerce.dto.ValidationError;
import com.alejfneto.dscomerce.services.exceptions.DataBaseException;
import com.alejfneto.dscomerce.services.exceptions.ForbiddenException;
import com.alejfneto.dscomerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler (ResourceNotFoundException.class)
	public ResponseEntity<CustomError> resorceNotFound(ResourceNotFoundException e, HttpServletRequest request){
	HttpStatus status = HttpStatus.NOT_FOUND;
	CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
	return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler (DataBaseException.class)
	public ResponseEntity<CustomError> dataBaseException(DataBaseException e, HttpServletRequest request){
	HttpStatus status = HttpStatus.BAD_REQUEST;
	CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
	return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler (MethodArgumentNotValidException.class)
	public ResponseEntity<CustomError> methodArgumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request){
	HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
	ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados Invalidos!", request.getRequestURI());
	for (FieldError fe : e.getBindingResult().getFieldErrors()) {
		err.addErrors(fe.getField(), fe.getDefaultMessage());
	}
	return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler (ForbiddenException.class)
	public ResponseEntity<CustomError> forbiddenException(ForbiddenException e, HttpServletRequest request){
	HttpStatus status = HttpStatus.FORBIDDEN;
	CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
	return ResponseEntity.status(status).body(err);
	}
	
}
