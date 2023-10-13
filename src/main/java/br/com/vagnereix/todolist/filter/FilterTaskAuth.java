package br.com.vagnereix.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.vagnereix.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    var servletPath = request.getServletPath();

    if (servletPath.startsWith("/tasks/")) {
      String auth = request.getHeader("Authorization");
      if (auth == null) {
        response.sendError(401, "User with no permission");
        return;
      }

      String authEncoded = auth.substring("Basic".length()).trim();
      byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
      String authString = new String(authDecoded);

      String[] credentials = authString.split(":");
      String username = credentials[0];
      String password = credentials[1];

      var user = this.userRepository.findByUsername(username);
      if (user == null) {
        response.sendError(401, "User with no permission");
        return;
      }

      var passwordVerify = BCrypt
        .verifyer()
        .verify(password.toCharArray(), user.getPassword());
      if (!passwordVerify.verified) {
        response.sendError(401, "User with no permission");
        return;
      }

      request.setAttribute("userId", user.getId());
      filterChain.doFilter(request, response);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
