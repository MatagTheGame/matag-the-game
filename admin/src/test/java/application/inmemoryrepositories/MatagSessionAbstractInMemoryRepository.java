package application.inmemoryrepositories;

import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
public class MatagSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<MatagSession, String> implements MatagSessionRepository {
  @Override
  public String getId(MatagSession matagSession) {
    return matagSession.getId();
  }

  @Override
  public void generateId(MatagSession session) {
    session.setId(UUID.randomUUID().toString());
  }

  @Override
  public long countOnlineUsers(LocalDateTime now) {
    return DATA.values().stream().filter(s -> s.getValidUntil().isAfter(now)).count();
  }

  @Override
  public void deleteValidUntilBefore(LocalDateTime now) {
    List<String> keysToRemove = DATA.entrySet().stream()
      .filter(e -> e.getValue().getValidUntil().isAfter(now))
      .map(Map.Entry::getKey)
      .collect(toList());

    keysToRemove.forEach(DATA::remove);
  }
}
