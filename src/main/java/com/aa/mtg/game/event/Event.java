package com.aa.mtg.game.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Event {
  private final String type;
  private final Object value;

  public Event(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public Event(String type) {
    this(type, null);
  }

  public String getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

}
