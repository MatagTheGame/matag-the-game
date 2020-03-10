package application;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class MockClock extends Clock {
  private Instant currentTime;

  @Override
  public ZoneId getZone() {
    return ZoneOffset.UTC;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return this;
  }

  @Override
  public Instant instant() {
    return currentTime;
  }

  public void setCurrentTime(Instant currentTime) {
    this.currentTime = currentTime;
  }
}
