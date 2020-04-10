package application.inmemoryrepositories;

import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;

@Component
public class MatagUserAbstractInMemoryRepository extends AbstractInMemoryRepository<MatagUser, Long> implements MatagUserRepository {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override
  protected Long getId(MatagUser matagUser) {
    return matagUser.getId();
  }

  @Override
  protected void generateId(MatagUser user) {
    user.setId(idGenerator.incrementAndGet());
  }

  @Override
  public Optional<MatagUser> findByEmailAddress(String email) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getEmailAddress().equals(email))
      .findFirst();
  }

  @Override
  public long countActiveUsers() {
    return DATA.values().stream().filter(u -> u.getStatus() == ACTIVE).count();
  }
}
