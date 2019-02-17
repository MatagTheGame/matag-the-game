package com.aa.mtg.event;

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
