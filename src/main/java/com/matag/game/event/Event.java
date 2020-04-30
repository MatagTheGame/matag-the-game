package com.matag.game.event;

import com.matag.game.player.Player;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class Event {
  private final String type;
  private final Object value;

  public Event(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public Event(String type, Player player, Object value) {
    this(type, map(player.getName(), value));
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

  private static Map<String, Object> map(String player, Object value) {
    Map<String, Object> map = new HashMap<>();
    map.put("playerName", player);
    map.put("value", value);
    return map;
  }
}
