package br.com.vagnereix.todolist.messages;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

  private String message;
  private int statusCode;

  public ErrorResponse() {
    this.statusCode = HttpStatus.BAD_REQUEST.value();
  }
}
