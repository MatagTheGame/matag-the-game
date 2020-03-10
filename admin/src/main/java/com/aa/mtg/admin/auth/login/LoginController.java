package com.aa.mtg.admin.auth.login;

import com.aa.mtg.admin.session.MtgSession;
import com.aa.mtg.admin.session.MtgSessionRepository;
import com.aa.mtg.admin.user.MtgUser;
import com.aa.mtg.admin.user.MtgUserRepository;
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

import static com.aa.mtg.admin.session.AuthSessionFilter.SESSION_DURATION_TIME;
import static com.aa.mtg.admin.user.MtgUserStatus.ACTIVE;

@RestController
@RequestMapping("/auth")
public class LoginController {
  private final MtgUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MtgSessionRepository mtgSessionRepository;
  private final Clock clock;

  public LoginController(MtgUserRepository userRepository, PasswordEncoder passwordEncoder, MtgSessionRepository mtgSessionRepository, Clock clock) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.mtgSessionRepository = mtgSessionRepository;
    this.clock = clock;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    Optional<MtgUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());

    if (!userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    MtgUser user = userOptional.get();
    if (user.getStatus() != ACTIVE) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    MtgSession session = MtgSession.builder()
      .id(UUID.randomUUID().toString())
      .mtgUser(user)
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME))
      .build();
    mtgSessionRepository.save(session);

    return ResponseEntity.ok(new LoginResponse(session.getId()));
  }
}
