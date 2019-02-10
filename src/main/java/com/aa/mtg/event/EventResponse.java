package com.aa.mtg.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class EventResponse {
  public enum ResponseStatus {
    SUCCESS, ERROR
  }

  private ResponseStatus responseStatus;
  private String message;
}
