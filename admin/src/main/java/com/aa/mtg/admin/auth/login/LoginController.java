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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.aa.mtg.admin.user.MtgUserStatus.ACTIVE;

@RestController
@RequestMapping("/auth")
public class LoginController {
  private final MtgUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MtgSessionRepository mtgSessionRepository;

  public LoginController(MtgUserRepository userRepository, PasswordEncoder passwordEncoder, MtgSessionRepository mtgSessionRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.mtgSessionRepository = mtgSessionRepository;
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

    String sessionId = UUID.randomUUID().toString();

    MtgSession session = new MtgSession(sessionId, user, LocalDateTime.now().plusDays(1));
    mtgSessionRepository.save(session);

    return ResponseEntity.ok(new LoginResponse(sessionId));
  }
}
