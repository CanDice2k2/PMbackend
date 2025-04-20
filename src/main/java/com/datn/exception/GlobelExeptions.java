package com.datn.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobelExeptions {
	
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetais> userEceptionHandler(UserException ue,
			WebRequest req){
		ErrorDetais error=new ErrorDetais(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		return new ResponseEntity<ErrorDetais>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetais> otherEceptionHandler(Exception ue,
			WebRequest req){
		ErrorDetais error=new ErrorDetais(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		return new ResponseEntity<ErrorDetais>(error,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LabelNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLabelNotFoundException(LabelNotFoundException ex) {
		ErrorResponse error = new ErrorResponse() {
			@Override
			public HttpStatusCode getStatusCode() {
				return null;
			}

			@Override
			public ProblemDetail getBody() {
				return null;
			}
		};
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
