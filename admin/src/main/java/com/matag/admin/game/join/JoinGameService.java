package com.matag.admin.game.join;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;

@Component
@AllArgsConstructor
public class JoinGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
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
      GamePlayers gamePlayers = gameSessionService.loadPlayers(game);
      if (gamePlayers.getOpponentSession() == null) {
        return game;
      }
    }

    return null;
  }
}
