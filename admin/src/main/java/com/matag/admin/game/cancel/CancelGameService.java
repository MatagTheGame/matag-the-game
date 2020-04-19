package com.matag.admin.game.cancel;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameResultType;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

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
    Optional<Game> optionalGame = gameRepository.findById(gameId);
    if (optionalGame.isPresent()) {
      Game game = optionalGame.get();
      GamePlayers gamePlayers = gameSessionService.loadPlayers(game);
      if (gamePlayers.getOpponentSession() == null) {
        gameSessionRepository.delete(gamePlayers.getPlayerSession());
        gameRepository.delete(game);

      } else {
        game.setStatus(GameStatusType.FINISHED);
        GameResultType gameResultType;
        if (gamePlayers.getPlayerSession().getSession().getId().equals(securityContextHolderHelper.getSession().getId())) {
          gameResultType = GameResultType.R2;
        } else {
          gameResultType = GameResultType.R1;
        }
        game.setResult(gameResultType);
        game.setFinishedAt(LocalDateTime.now(clock));
        gameRepository.save(game);

        gamePlayers.getPlayerSession().setSession(null);
        gameSessionRepository.save(gamePlayers.getPlayerSession());
        gamePlayers.getOpponentSession().setSession(null);
        gameSessionRepository.save(gamePlayers.getOpponentSession());
      }
    }

    return CancelGameResponse.builder().build();
  }
}
