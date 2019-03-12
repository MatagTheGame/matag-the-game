package com.aa.mtg.game.turn.events;

public class LifeEvent {
    private final String playerName;
    private final int life;

    public LifeEvent(String playerName, int life) {
        this.playerName = playerName;
        this.life = life;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLife() {
        return life;
    }
}
