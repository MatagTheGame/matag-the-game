package com.aa.mtg.game.turn;

import java.util.List;

public class TurnRequest {
    private String action;
    private String triggeredAction;
    private List<Integer> cardIds;
    private List<Integer> tappingLandIds;

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

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    public List<Integer> getTappingLandIds() {
        return tappingLandIds;
    }

    public void setTappingLandIds(List<Integer> tappingLandIds) {
        this.tappingLandIds = tappingLandIds;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                ", triggeredAction='" + triggeredAction + '\'' +
                ", cardIds=" + cardIds +
                ", tappingLandIds=" + tappingLandIds +
                '}';
    }
}
