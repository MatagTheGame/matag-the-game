package com.matag.admin.stats;

import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.MatagUserStatus;
import com.matag.cards.sets.MtgSets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;

@Component
@AllArgsConstructor
public class StatsService {
  private final MatagUserRepository matagUserRepository;
  private final MatagSessionRepository matagSessionRepository;
  private final MtgSets mtgSets;
  private final Clock clock;

  public long countTotalUsers() {
    return matagUserRepository.countUsersByStatus(ACTIVE);
  }

  public long countOnlineUsers() {
    return matagSessionRepository.countOnlineUsers(LocalDateTime.now(clock));
  }

  public int countCards() {
    return mtgSets.countCards();
  }

  public int countSets() {
    return mtgSets.getSets().size();
  }
}
