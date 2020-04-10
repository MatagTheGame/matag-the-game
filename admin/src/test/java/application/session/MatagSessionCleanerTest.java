package application.session;

import static application.TestUtils.user1;
import static application.TestUtils.user2;
import static org.assertj.core.api.Assertions.assertThat;

import application.AbstractApplicationTest;
import com.matag.admin.session.MatagSessionCleaner;
import java.util.UUID;

import com.matag.admin.session.MatagSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MatagSessionCleanerTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionCleaner matagSessionCleaner;

  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Test
  public void cleanupOldSessions() {
    // Given
    loginUser(UUID.randomUUID().toString(), user1());
    setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1));
    loginUser(UUID.randomUUID().toString(), user2());

    // When
    matagSessionCleaner.cleanupOldSessions();

    // Then
    assertThat(matagSessionRepository.count()).isEqualTo(1);
  }
}