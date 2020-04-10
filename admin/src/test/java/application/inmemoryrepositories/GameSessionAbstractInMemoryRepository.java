package application.inmemoryrepositories;

import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class GameSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<GameSession, Long> implements GameSessionRepository {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override
  protected Long getId(GameSession gameSession) {
    return gameSession.getId();
  }

  @Override
  protected void generateId(GameSession gameSession) {
    gameSession.setId(idGenerator.incrementAndGet());
  }
}
