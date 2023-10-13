package br.com.vagnereix.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "users")
public class UserModel {

  @Id
  @Column(unique = true)
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(unique = true)
  private String username;

  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
