package application.inmemoryrepositories;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.game.GameType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

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
  public List<Game> findByTypeAndStatus(GameType type, GameStatusType status) {
    return findAll().stream()
      .filter(g -> g.getType().equals(type))
      .filter(g -> g.getStatus().equals(status))
      .collect(toList());
  }
}
