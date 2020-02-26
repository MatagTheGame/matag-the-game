package com.aa.mtg.admin.authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    return new LoginResponse("token");
  }
}
