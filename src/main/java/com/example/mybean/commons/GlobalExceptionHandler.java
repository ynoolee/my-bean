package com.example.mybean.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.mybean.commons.exception.NotFoundException;
import com.example.mybean.product.exception.StockOutException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception e) {
		log.error("", e);

		return ResponseEntity.internalServerError().body(e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("{}", e.getMessage(), e);

		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
		log.info("{}", e.getMessage(), e);

		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(StockOutException.class)
	public ResponseEntity<String> handleStockException(StockOutException e) {
		log.info("{}", e.getMessage(), e);

		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
		String error = "request body 가 비어있거나 옳바르지 않은 JSON request 가 포함되어있습니다";

		log.error("{}", error,ex);

		return ResponseEntity.badRequest().body(error);
	}
}
