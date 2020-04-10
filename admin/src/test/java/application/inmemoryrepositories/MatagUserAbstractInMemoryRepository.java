package application.inmemoryrepositories;

import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.MatagUserStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MatagUserAbstractInMemoryRepository extends AbstractInMemoryRepository<MatagUser, Long> implements MatagUserRepository {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override
  public Long getId(MatagUser matagUser) {
    return matagUser.getId();
  }

  @Override
  public void generateId(MatagUser user) {
    user.setId(idGenerator.incrementAndGet());
  }

  @Override
  public void resetGenerator() {
    idGenerator.set(0);
  }

  @Override
  public Optional<MatagUser> findByEmailAddress(String email) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getEmailAddress().equals(email))
      .findFirst();
  }

  @Override
  public long countUsersByStatus(MatagUserStatus matagUserStatus) {
    return DATA.values().stream().filter(u -> u.getStatus() == matagUserStatus).count();
  }
}
