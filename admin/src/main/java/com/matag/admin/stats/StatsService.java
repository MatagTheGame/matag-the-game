package com.matag.admin.stats;

import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUserRepository;
import com.matag.cards.sets.MtgSets;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class StatsService {
  private final MatagUserRepository matagUserRepository;
  private final MatagSessionRepository matagSessionRepository;
  private final MtgSets mtgSets;
  private final Clock clock;

  public StatsService(MatagUserRepository matagUserRepository, MatagSessionRepository matagSessionRepository, MtgSets mtgSets, Clock clock) {
    this.matagUserRepository = matagUserRepository;
    this.matagSessionRepository = matagSessionRepository;
    this.mtgSets = mtgSets;
    this.clock = clock;
  }

  public long countTotalUsers() {
    return matagUserRepository.countActiveUsers();
  }

  public long countOnlineUsers() {
    return matagSessionRepository.countOnlineUsers(LocalDateTime.now(clock));
  }

  public int countCards() {
    return mtgSets.countCards();
  }
}
