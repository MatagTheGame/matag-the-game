package com.matag.admin.game.cancel;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Component
@AllArgsConstructor
public class CancelGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
  private final Clock clock;

  @Transactional
  public CancelGameResponse cancel(Long gameId) {
    System.out.println();
    return CancelGameResponse.builder()
      .error("Functionality not yet implemented.")
      .build();
  }
}
