package com.bank.transfers.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountAlreadyExists.class)
  public ResponseEntity<String> handleAccountNotFound(AccountAlreadyExists ex){
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex){
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IdempotencyConflictException.class)
  public ResponseEntity<String> handleIdempotencyKeyConflict(IdempotencyConflictException ex){
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

}
