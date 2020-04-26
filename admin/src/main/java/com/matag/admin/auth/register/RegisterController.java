package com.matag.admin.auth.register;

import com.matag.admin.auth.login.EmailRegexMatcher;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegisterController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  public static final String EMAIL_IS_INVALID = "Email is invalid.";
  public static final String PASSWORD_IS_INVALID = "Password is invalid.";
  public static final String EMAIL_ALREADY_REGISTERED = "This email is already registered (use reset password functionality).";
  public static final String USERNAME_ALREADY_REGISTERED = "This username is already registered (please choose a new one).";
  public static final String REGISTERED_VERIFY_EMAIL = "Please check your email for a verification code.";

  private final Clock clock;
  private final MatagUserRepository userRepository;
  private final EmailRegexMatcher emailRegexMatcher;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
    LOGGER.info("User " + request.getEmail() + " registering.");

    if (!emailRegexMatcher.isValidEmail(request.getEmail())) {
      return response(BAD_REQUEST, EMAIL_IS_INVALID);
    }

    if (request.getPassword().length() < 4) {
      return response(BAD_REQUEST, PASSWORD_IS_INVALID);
    }

    if (userRepository.findByEmailAddress(request.getEmail()).isPresent()) {
      return response(BAD_REQUEST, EMAIL_ALREADY_REGISTERED);
    }

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      return response(BAD_REQUEST, USERNAME_ALREADY_REGISTERED);
    }

    MatagUser matagUser = new MatagUser();
    matagUser.setUsername(request.getUsername());
    matagUser.setPassword(passwordEncoder.encode(request.getPassword()));
    matagUser.setEmailAddress(request.getEmail());
    matagUser.setCreatedAt(LocalDateTime.now(clock));
    userRepository.save(matagUser);

    return response(OK, REGISTERED_VERIFY_EMAIL);
  }

  private ResponseEntity<RegisterResponse> response(HttpStatus status, String message) {
    LOGGER.info("Registration failed with status=" + status + "; error=" + message);
    return ResponseEntity
      .status(status)
      .body(RegisterResponse.builder()
        .message(message)
        .build());
  }
}
