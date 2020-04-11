package com.matag.admin.game;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
  public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
    MatagUser user = securityContextHolderHelper.getUser();
    MatagSession session = securityContextHolderHelper.getSession();

    Optional<GameSession> activeGameOfPlayer = gameSessionRepository.findPlayerActiveGameSession(session.getId());
    if (activeGameOfPlayer.isPresent()) {
      Long activeGameId = activeGameOfPlayer.get().getGame().getId();
      return JoinGameResponse.builder()
        .errorMessage("You are already in a game.")
        .activeGameId(activeGameId)
        .build();
    }

    List<Game> games = gameRepository.findByTypeAndStatus(joinGameRequest.getGameType(), STARTING);
    Game freeGame = findFreeGame(games);

    if (freeGame == null) {
      freeGame = Game.builder()
        .createdAt(LocalDateTime.now(clock))
        .type(joinGameRequest.getGameType())
        .status(STARTING)
        .build();
    } else {
      freeGame.setStatus(IN_PROGRESS);
    }

    gameRepository.save(freeGame);

    GameSession gameSession = GameSession.builder()
      .game(freeGame)
      .session(session)
      .player(user)
      .playerOptions(joinGameRequest.getPlayerOptions())
      .build();

    gameSessionRepository.save(gameSession);

    return JoinGameResponse.builder()
      .gameId(freeGame.getId())
      .build();
  }

  private Game findFreeGame(List<Game> games) {
    for (Game game : games) {
      List<GameSession> gameSession = gameSessionRepository.findByGame(game);

      if (gameSession.size() >= 1) {
        return game;
      }
    }

    return null;
  }
}
