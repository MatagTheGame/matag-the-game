package com.aa.mtg.game.event;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return type.equals(event.type) &&
            Objects.equals(value, event.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public String toString() {
    return "Event{" +
            "type='" + type + '\'' +
            ", value=" + value +
            '}';
  }
}
