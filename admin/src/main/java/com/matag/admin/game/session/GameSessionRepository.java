package com.matag.admin.game.session;

import com.matag.admin.game.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, Long> {

  List<GameSession> findByGame(Game game);
}
