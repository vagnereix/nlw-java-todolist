package br.com.vagnereix.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "tasks")
public class TaskModel {

  @Id
  @Column(unique = true)
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private UUID userId;

  @Column(length = 50)
  private String title;

  private String description;
  private String priority;
  private LocalDateTime startAt;
  private LocalDateTime finishAt;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if (title.length() > 50) {
      throw new Exception("Title must contain 50 characters or less");
    }

    this.title = title;
  }
}
