package com.test.demo.exceptionHandler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class exceptionHandler {
		@ExceptionHandler(value=RuntimeException.class)
		ResponseEntity<String> handlingRuntimeException (RuntimeException exception){
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
		@ExceptionHandler(value= MethodArgumentNotValidException.class)
		ResponseEntity<String> handlingRuntimeException (MethodArgumentNotValidException exception){
			return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
		}
}
