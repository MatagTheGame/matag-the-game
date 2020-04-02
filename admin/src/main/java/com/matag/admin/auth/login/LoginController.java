package com.matag.admin.auth.login;

import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.session.AuthSessionFilter;
import com.matag.admin.user.MatagUserStatus;
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

@RestController
@RequestMapping("/auth")
public class LoginController {
  private final MatagUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  public LoginController(MatagUserRepository userRepository, PasswordEncoder passwordEncoder, MatagSessionRepository matagSessionRepository, Clock clock) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.matagSessionRepository = matagSessionRepository;
    this.clock = clock;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    Optional<MatagUser> userOptional = userRepository.findByEmailAddress(loginRequest.getEmail());

    if (!userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    MatagUser user = userOptional.get();
    if (user.getStatus() != MatagUserStatus.ACTIVE) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    MatagSession session = MatagSession.builder()
      .id(UUID.randomUUID().toString())
      .matagUser(user)
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME))
      .build();
    matagSessionRepository.save(session);

    return ResponseEntity.ok(new LoginResponse(session.getId()));
  }
}
