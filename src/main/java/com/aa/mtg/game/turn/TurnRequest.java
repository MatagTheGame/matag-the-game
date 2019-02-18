package com.aa.mtg.game.turn;

public class TurnRequest {
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                '}';
    }
}
