package com.aa.mtg.admin.auth;

import com.aa.mtg.admin.user.User;
import com.aa.mtg.admin.user.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.getUsername());
    return new LoginResponse(user.getPassword());
  }
}
