package com.matag.admin.stats;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
public class StatsController {
  private final StatsService statsService;

  @GetMapping
  public StatsResponse stats() {
    return StatsResponse.builder()
      .totalUsers(statsService.countTotalUsers())
      .onlineUsers(statsService.countOnlineUsers())
      .totalCards(statsService.countCards())
      .totalSets(statsService.countSets())
      .build();
  }
}
