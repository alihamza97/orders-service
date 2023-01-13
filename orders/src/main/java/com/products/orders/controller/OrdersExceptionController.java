package com.products.orders.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.products.orders.exception.ErrorResponse;
import com.products.orders.exception.OrdersException;

@ControllerAdvice
public class OrdersExceptionController {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> forbiddenRequestException(OrdersException invalidOrderException) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
		errorResponse.setMessage(invalidOrderException.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(exception.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
