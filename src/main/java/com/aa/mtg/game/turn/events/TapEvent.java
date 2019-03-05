package com.aa.mtg.game.turn.events;

public class TapEvent {
    private final String playerName;
    private final long cardId;

    public TapEvent(String playerName, long cardId) {
        this.playerName = playerName;
        this.cardId = cardId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getCardId() {
        return cardId;
    }
}
