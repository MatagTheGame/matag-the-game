package application.inmemoryrepositories;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;
import static java.util.stream.Collectors.toList;

@Component
public class GameSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<GameSession, Long> implements GameSessionRepository {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override
  public Long getId(GameSession gameSession) {
    return gameSession.getId();
  }

  @Override
  public void generateId(GameSession gameSession) {
    gameSession.setId(idGenerator.incrementAndGet());
  }

  @Override
  public void resetGenerator() {
    idGenerator.set(0);
  }

  @Override
  public List<GameSession> findByGame(Game game) {
    return findAll().stream()
      .filter(gs -> gs.getGame().getId().equals(game.getId()))
      .collect(toList());
  }

  @Override
  public Optional<GameSession> findPlayerActiveGameSession(String session) {
    return findAll().stream()
      .filter(gs -> gs.getSession().getId().equals(session))
      .filter(gs -> gs.getGame().getStatus() == STARTING || gs.getGame().getStatus() == IN_PROGRESS)
      .findFirst();
  }
}
