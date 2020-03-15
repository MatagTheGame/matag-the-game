package com.matag.admin.stats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class StatsResponse {
  private final long totalUsers;
  private final long onlineUsers;
  private final int totalCards;

  @JsonCreator
  public StatsResponse(@JsonProperty("totalUsers") long totalUsers, @JsonProperty("onlineUsers") long onlineUsers, @JsonProperty("totalCards") int totalCards) {
    this.totalUsers = totalUsers;
    this.onlineUsers = onlineUsers;
    this.totalCards = totalCards;
  }
}
