package com.aa.mtg.game.turn;

public class TurnRequest {
    private String action;
    private String cardId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
