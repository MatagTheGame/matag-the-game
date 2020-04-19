package com.matag.admin.stats;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = StatsResponse.StatsResponseBuilder.class)
@Builder(toBuilder = true)
public class StatsResponse {
  private final long totalUsers;
  private final long onlineUsers;
  private final int totalCards;
  private final int totalSets;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StatsResponseBuilder {

  }
}
