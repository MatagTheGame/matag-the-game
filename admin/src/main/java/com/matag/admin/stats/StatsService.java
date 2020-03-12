package com.matag.admin.stats;

import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUserRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class StatsService {
  private final MatagUserRepository matagUserRepository;
  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  public StatsService(MatagUserRepository matagUserRepository, MatagSessionRepository matagSessionRepository, Clock clock) {
    this.matagUserRepository = matagUserRepository;
    this.matagSessionRepository = matagSessionRepository;
    this.clock = clock;
  }

  public long countTotalUsers() {
    return matagUserRepository.countActiveUsers();
  }

  public long countOnlineUsers() {
    return matagSessionRepository.countOnlineUsers(LocalDateTime.now(clock));
  }
}
