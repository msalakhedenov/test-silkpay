package com.sm.testsilkpay.exception.handler;

import com.sm.testsilkpay.exception.AuthException;
import com.sm.testsilkpay.exception.BankAccountNotFoundException;
import com.sm.testsilkpay.exception.InsufficientFundsException;
import com.sm.testsilkpay.model.web.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlingRestControllerAdvice {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ErrorMessage> authException(AuthException e) {
    return ResponseEntity.status(e.getStatus())
                         .body(ErrorMessage.of(e.getMessage()));
  }

  @ExceptionHandler(BankAccountNotFoundException.class)
  public ResponseEntity<ErrorMessage> bankAccountNotFoundException(BankAccountNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                         .body(ErrorMessage.of(e.getMessage()));
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ErrorMessage> insufficientFundsException(InsufficientFundsException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(ErrorMessage.of(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> validationExceptions(MethodArgumentNotValidException e) {
    Map<String, String> fieldErrors = new HashMap<>();

    e.getAllErrors().forEach(error -> fieldErrors.put(((FieldError) error).getField(), error.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(fieldErrors);
  }

}
