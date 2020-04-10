package com.matag.admin.game;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.matag.admin.game.GameStatusType.STARTING;

@Component
@AllArgsConstructor
public class GameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final Clock clock;

  @Transactional
  public long joinGame(GameType gameType, String playerOptions) {
    Game game = Game.builder()
      .createdAt(LocalDateTime.now(clock))
      .type(gameType)
      .status(STARTING)
      .build();

    gameRepository.save(game);

    GameSession gameSession = GameSession.builder()
      .game(game)
      .sessionNum(1)
      .session(securityContextHolderHelper.getSession())
      .player(securityContextHolderHelper.getUser())
      .playerOptions(playerOptions)
      .build();

    gameSessionRepository.save(gameSession);

    return game.getId();
  }
}
