package com.matag.admin.game;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.matag.admin.game.GameStatusType.IN_PROGRESS;
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
    List<Game> games = gameRepository.findByTypeAndStatus(gameType, STARTING);
    Game freeGame = findFreeGame(games);

    if (freeGame == null) {
      freeGame = Game.builder()
        .createdAt(LocalDateTime.now(clock))
        .type(gameType)
        .status(STARTING)
        .build();
    } else {
      freeGame.setStatus(IN_PROGRESS);
    }

    gameRepository.save(freeGame);

    GameSession gameSession = GameSession.builder()
      .game(freeGame)
      .session(securityContextHolderHelper.getSession())
      .player(securityContextHolderHelper.getUser())
      .playerOptions(playerOptions)
      .build();

    gameSessionRepository.save(gameSession);

    return freeGame.getId();
  }

  private Game findFreeGame(List<Game> games) {
    for (Game game : games) {
      List<GameSession> gameSession = gameSessionRepository.findByGame(game);

      if (gameSession.size() == 1) {
        return game;
      }
    }

    return null;
  }
}
