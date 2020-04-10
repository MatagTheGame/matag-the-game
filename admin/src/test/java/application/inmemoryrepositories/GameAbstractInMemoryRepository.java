package application.inmemoryrepositories;

import com.matag.admin.game.Game;
import com.matag.admin.game.GameRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class GameAbstractInMemoryRepository extends AbstractInMemoryRepository<Game, Long> implements GameRepository {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override
  public Long getId(Game game) {
    return game.getId();
  }

  @Override
  public void generateId(Game game) {
    game.setId(idGenerator.incrementAndGet());
  }

  @Override
  public void resetGenerator() {
    idGenerator.set(0);
  }

  @Override
  public List<Game> findByTypeAndStatus() {
    return null;
  }
}
