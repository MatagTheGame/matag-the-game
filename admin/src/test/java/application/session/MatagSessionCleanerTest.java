package application.session;

import static org.assertj.core.api.Assertions.assertThat;

import application.AbstractApplicationTest;
import com.matag.admin.session.MatagSessionCleaner;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MatagSessionCleanerTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionCleaner matagSessionCleaner;

  @Test
  public void cleanupOldSessions() {
    // Given
    setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1));
    loginUser(UUID.randomUUID().toString());

    // When
    matagSessionCleaner.cleanupOldSessions();

    // Then
    assertThat(matagSessionRepository.count()).isEqualTo(1);
  }
}