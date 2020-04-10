package com.matag.admin.game.session;

import com.matag.admin.game.Game;
import com.matag.admin.user.MatagUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, Long> {
  List<GameSession> findByGame(Game game);

  @Query("FROM GameSession gs WHERE gs.player = ?1 AND gs.game.status IN ('STARTING', 'IN_PROGRESS')")
  Optional<GameSession> findPlayerActiveGameSession(MatagUser player);
}
