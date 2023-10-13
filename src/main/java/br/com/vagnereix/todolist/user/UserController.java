package br.com.vagnereix.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.vagnereix.todolist.messages.SuccessResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody UserModel userModel)
    throws Exception {
    var user = userRepository.findByUsername(userModel.getUsername());

    if (user != null) {
      throw new Exception("User already exists");
    }

    var passwordHashed = BCrypt
      .withDefaults()
      .hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(passwordHashed);

    var userCreated = this.userRepository.save(userModel);
    URI uri = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(userCreated.getId())
      .toUri();

    var successResponse = new SuccessResponse();
    successResponse.setData(userCreated);

    return ResponseEntity.created(uri).body(successResponse);
  }
}
