package com.aa.mtg.game.event;

public abstract class EventResponse {
  public enum ResponseStatus {
    SUCCESS, ERROR
  }

  private final ResponseStatus responseStatus;
  private final String message;

  public EventResponse(ResponseStatus responseStatus, String message) {
    this.responseStatus = responseStatus;
    this.message = message;
  }

  public ResponseStatus getResponseStatus() {
    return responseStatus;
  }

  public String getMessage() {
    return message;
  }
}
