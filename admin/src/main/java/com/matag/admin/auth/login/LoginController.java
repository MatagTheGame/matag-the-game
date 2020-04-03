package com.matag.admin.auth.login;

import com.matag.admin.session.AuthSessionFilter;
import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.MatagUserStatus;
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
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  public static final String EMAIL_IS_INVALID = "Email is invalid.";
  public static final String PASSWORD_IS_INVALID = "Password is invalid.";
  public static final String EMAIL_OR_PASSWORD_ARE_INCORRECT = "Email or password are not correct.";
  public static final String ACCOUNT_IS_NOT_ACTIVE = "Account is not active.";

  private final MatagUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MatagSessionRepository matagSessionRepository;
  private final EmailRegexMatcher emailRegexMatcher;
  private final Clock clock;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    LOGGER.info("User " + loginRequest.getEmail() + " logging in.");

    if (!emailRegexMatcher.isValidEmail(loginRequest.getEmail())) {
      return response(BAD_REQUEST, EMAIL_IS_INVALID);
    }

    if (loginRequest.getPassword().length() < 4) {
      return response(BAD_REQUEST, PASSWORD_IS_INVALID);
    }

    Optional<MatagUser> userOptional = userRepository.findByEmailAddress(loginRequest.getEmail());

    if (!userOptional.isPresent()) {
      return response(UNAUTHORIZED, EMAIL_OR_PASSWORD_ARE_INCORRECT);
    }

    MatagUser user = userOptional.get();
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return response(UNAUTHORIZED, EMAIL_OR_PASSWORD_ARE_INCORRECT);
    }

    if (user.getStatus() != MatagUserStatus.ACTIVE) {
      return response(UNAUTHORIZED, ACCOUNT_IS_NOT_ACTIVE);
    }

    MatagSession session = MatagSession.builder()
      .id(UUID.randomUUID().toString())
      .matagUser(user)
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME))
      .build();
    matagSessionRepository.save(session);

    LOGGER.info("Login successful.");
    return ResponseEntity.ok(new LoginResponse(session.getId(), null));
  }

  private ResponseEntity<LoginResponse> response(HttpStatus status, String error) {
    LOGGER.info("Login failed with status=" + status + "; error=" + error);
    return ResponseEntity.status(status).body(new LoginResponse(null, error));
  }
}
