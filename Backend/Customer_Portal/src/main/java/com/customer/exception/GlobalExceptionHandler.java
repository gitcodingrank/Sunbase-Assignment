package com.customer.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<MyErrorDetail> userNotFoundExceptionHandler(CustomerNotFoundException cnfe, WebRequest wr){
		
		MyErrorDetail error = new MyErrorDetail(LocalDateTime.now(), cnfe.getMessage(), wr.getDescription(false));
		return new ResponseEntity<MyErrorDetail>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SomethingWentWrongException.class)
	public ResponseEntity<MyErrorDetail> todoNotFoundExceptionHandler(SomethingWentWrongException swre, WebRequest wr){
		
		MyErrorDetail error = new MyErrorDetail(LocalDateTime.now(), swre.getMessage(), wr.getDescription(false));
		return new ResponseEntity<MyErrorDetail>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MyErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException mrne, WebRequest wr){
		
		List<ObjectError> allErrors = mrne.getAllErrors();
		List<String> errorsToStringList = MethodArgumentNotValidException.errorsToStringList(allErrors);
		MyErrorDetail error = new MyErrorDetail(LocalDateTime.now(),String.join(",", errorsToStringList), wr.getDescription(false));
		
		return new ResponseEntity<MyErrorDetail>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorDetail> allExceptionHandler(Exception ex, WebRequest wr){
		
		MyErrorDetail error = new MyErrorDetail(LocalDateTime.now(), ex.getMessage(), wr.getDescription(false));
		return new ResponseEntity<MyErrorDetail>(error, HttpStatus.BAD_REQUEST);
	}
	

}
