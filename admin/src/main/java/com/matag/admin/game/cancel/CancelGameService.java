package com.matag.admin.game.cancel;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.finish.FinishGameService;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.session.MatagSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CancelGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
  private final FinishGameService finishGameService;

  @Transactional
  public CancelGameResponse cancel(Long gameId) {
    MatagSession session = securityContextHolderHelper.getSession();
    Optional<GameSession> activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.getId());
    if (activeGameSession.isPresent()) {
      if (activeGameSession.get().getGame().getId().equals(gameId)) {
        Game game = activeGameSession.get().getGame();
        GamePlayers gamePlayers = gameSessionService.loadPlayers(game);
        if (gamePlayers.getOpponentSession() == null) {
          gameSessionRepository.delete(gamePlayers.getPlayerSession());
          gameRepository.delete(game);

        } else {
          String winnerSessionId = findOpponentSessionId(gamePlayers, session);
          finishGameService.finishGame(game, gamePlayers, winnerSessionId);
        }
      }
    }

    return CancelGameResponse.builder().build();
  }

  private String findOpponentSessionId(GamePlayers gamePlayers, MatagSession session) {
    if (gamePlayers.getPlayerSession().getSession().getId().equals(session.getId())) {
      return gamePlayers.getOpponentSession().getSession().getId();
    } else {
      return gamePlayers.getPlayerSession().getSession().getId();
    }
  }
}
