package br.com.vagnereix.todolist.task;

import br.com.vagnereix.todolist.messages.SuccessResponse;
import br.com.vagnereix.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity<?> create(
    @RequestBody TaskModel taskModel,
    HttpServletRequest request,
    HttpServletResponse response
  ) throws Exception {
    var userId = (UUID) request.getAttribute("userId");
    taskModel.setUserId(userId);

    var currentDate = LocalDateTime.now();

    if (
      currentDate.isAfter(taskModel.getStartAt()) ||
      currentDate.isAfter(taskModel.getFinishAt())
    ) {
      throw new Exception("Start/end date must be after current date");
    }
    if (taskModel.getStartAt().isAfter(taskModel.getFinishAt())) {
      throw new Exception("Start date must be before end date");
    }

    var taskCreated = this.taskRepository.save(taskModel);
    URI uri = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(taskCreated.getId())
      .toUri();

    response.setHeader("Location", uri.toASCIIString());

    var successResponse = new SuccessResponse();
    successResponse.setMessage("Task created successfully");
    successResponse.setData(taskCreated);

    return ResponseEntity.created(uri).body(successResponse);
  }

  @GetMapping("/")
  public ResponseEntity<SuccessResponse> list(HttpServletRequest request)
    throws Exception {
    try {
      var userId = (UUID) request.getAttribute("userId");
      var tasks = this.taskRepository.findByUserId(userId);

      var successResponse = new SuccessResponse();
      successResponse.setData(tasks);

      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      throw new Exception("Error while listing tasks");
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> update(
    @RequestBody TaskModel taskModel,
    HttpServletRequest request,
    @PathVariable UUID id
  ) throws Exception, HttpMessageNotReadableException {
    try {
      var task = this.taskRepository.findById(id).orElse(null);

      var userId = (UUID) request.getAttribute("userId");
      if (!task.getUserId().equals(userId)) {
        throw new Exception("User not allowed to update this task");
      }

      Utils.copyNonNullProperties(taskModel, task);

      var updatedTask = this.taskRepository.save(task);

      var successResponse = new SuccessResponse();
      successResponse.setMessage("Task updated successfully");
      successResponse.setData(updatedTask);

      return ResponseEntity.ok(successResponse);
    } catch (NullPointerException e) {
      throw new Exception("Task not found");
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}
