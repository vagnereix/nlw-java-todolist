package br.com.vagnereix.todolist.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessResponse {

  private Object data;
  private String message;
}
