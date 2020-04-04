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
    long totalUsers = statsService.countTotalUsers();
    long onlineUsers = statsService.countOnlineUsers();
    int totalCards = statsService.countCards();
    return new StatsResponse(totalUsers, onlineUsers, totalCards);
  }
}
