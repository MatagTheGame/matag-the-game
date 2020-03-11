package com.matag.admin.stats;

import lombok.Value;

@Value
public class StatsResponse {
  private final int totalUsers;
  private final int onlineUsers;
}
