package com.matag.admin.game.finish;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameResultType;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.adminentities.FinishGameRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;

@Component
@AllArgsConstructor
public class FinishGameService {
  private final Clock clock;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;

  @Transactional
  public void finish(Long gameId, FinishGameRequest request) {
    Optional<Game> gameOptional = gameRepository.findById(gameId);
    gameOptional.ifPresent(game -> {
      if (game.getStatus() == IN_PROGRESS) {
        GamePlayers gamePlayers = gameSessionService.loadPlayers(game);
        finishGame(game, gamePlayers, request.getWinnerSessionId());
      }
    });
  }

  public void finishGame(Game game, GamePlayers gamePlayers, String winnerSessionId) {
    game.setStatus(GameStatusType.FINISHED);

    GameResultType gameResultType = getResult(gamePlayers, winnerSessionId);
    game.setResult(gameResultType);
    game.setFinishedAt(LocalDateTime.now(clock));
    gameRepository.save(game);

    gamePlayers.getPlayerSession().setSession(null);
    gameSessionRepository.save(gamePlayers.getPlayerSession());
    gamePlayers.getOpponentSession().setSession(null);
    gameSessionRepository.save(gamePlayers.getOpponentSession());
  }

  private GameResultType getResult(GamePlayers gamePlayers, String winnerSessionId) {
    if (gamePlayers.getPlayerSession().getSession().getId().equals(winnerSessionId)) {
      return GameResultType.R1;
    } else {
      return GameResultType.R2;
    }
  }
}
