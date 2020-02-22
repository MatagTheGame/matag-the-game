package com.aa.mtg.game.message;

public class MessageEvent {
  private final String text;
  private final boolean closable;

  public MessageEvent(String text, boolean closable) {
    this.text = text;
    this.closable = closable;
  }

  public String getText() {
    return text;
  }

  public boolean isClosable() {
    return closable;
  }
}
