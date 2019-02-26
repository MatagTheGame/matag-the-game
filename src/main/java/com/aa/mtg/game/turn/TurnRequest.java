package com.aa.mtg.game.turn;

public class TurnRequest {
    private String action;
    private String type;
    private int cardId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                ", type='" + type + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}
