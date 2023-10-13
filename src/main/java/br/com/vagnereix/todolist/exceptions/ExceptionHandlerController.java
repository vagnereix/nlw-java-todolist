package br.com.vagnereix.todolist.exceptions;

import br.com.vagnereix.todolist.messages.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    var errorResponse = new ErrorResponse();
    errorResponse.setMessage(e.getMessage());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> handleNullPointerException(
    NullPointerException e
  ) {
    var errorResponse = new ErrorResponse();
    errorResponse.setMessage(e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(
    HttpMessageNotReadableException e
  ) {
    var errorResponse = new ErrorResponse();
    errorResponse.setMessage(e.getMostSpecificCause().getMessage());

    return ResponseEntity.badRequest().body(errorResponse);
  }
}
