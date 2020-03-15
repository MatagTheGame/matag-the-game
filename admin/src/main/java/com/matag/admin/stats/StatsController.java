package com.matag.admin.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {
  private final StatsService statsService;

  public StatsController(StatsService statsService) {
    this.statsService = statsService;
  }

  @GetMapping
  public StatsResponse stats() {
    long totalUsers = statsService.countTotalUsers();
    long onlineUsers = statsService.countOnlineUsers();
    int totalCards = statsService.countCards();
    return new StatsResponse(totalUsers, onlineUsers, totalCards);
  }
}
