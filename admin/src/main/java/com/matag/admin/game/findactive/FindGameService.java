package com.matag.admin.game.findactive;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.session.MatagSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FindGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;

  public ActiveGameResponse findActiveGame() {
    MatagSession session = securityContextHolderHelper.getSession();
    Optional<GameSession> activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.getId());

    if (activeGameSession.isEmpty()) {
      return ActiveGameResponse.builder().build();
    }

    Game game = activeGameSession.get().getGame();
    GamePlayers gamePlayers = gameSessionService.loadPlayers(game);

    return ActiveGameResponse.builder()
      .gameId(game.getId())
      .createdAt(game.getCreatedAt())
      .playerName(gamePlayers.getPlayerSession().getPlayer().getUsername())
      .playerOptions(gamePlayers.getPlayerSession().getPlayerOptions())
      .opponentName(gamePlayers.getOpponentSession() != null ? gamePlayers.getOpponentSession().getPlayer().getUsername() : null)
      .opponentOptions(gamePlayers.getOpponentSession() != null ? gamePlayers.getOpponentSession().getPlayerOptions() : null)
      .build();
  }
}
