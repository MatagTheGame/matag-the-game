package com.matag.admin.session;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MatagSessionCleaner {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatagSessionCleaner.class);

  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  @Scheduled(fixedRate = 6 * 60 * 60 * 1000, initialDelay = 500)
  public void cleanupOldSessions() {
    LOGGER.info("cleanupOldSessions started");
    matagSessionRepository.deleteValidUntilBefore(LocalDateTime.now(clock));
  }
}
