package com.aa.mtg.game.turn;

public class TurnRequest {
    private String action;
    private String triggeredAction;
    private int cardId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTriggeredAction() {
        return triggeredAction;
    }

    public void setTriggeredAction(String triggeredAction) {
        this.triggeredAction = triggeredAction;
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
                ", triggeredAction='" + triggeredAction + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}
