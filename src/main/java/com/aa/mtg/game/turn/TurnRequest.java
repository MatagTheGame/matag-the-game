package com.aa.mtg.game.turn;

import java.util.List;
import java.util.Map;

public class TurnRequest {
    private String action;
    private String triggeredAction;
    private List<Integer> tappingLandIds;
    private List<Integer> cardIds;
    private Map<Integer, List<Integer>> targetsIdsForCardIds;

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

    public Map<Integer, List<Integer>> getTargetsIdsForCardIds() {
        return targetsIdsForCardIds;
    }

    public void setTargetsIdsForCardIds(Map<Integer, List<Integer>> targetsIdsForCardIds) {
        this.targetsIdsForCardIds = targetsIdsForCardIds;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                ", triggeredAction='" + triggeredAction + '\'' +
                ", tappingLandIds=" + tappingLandIds +
                ", cardIds=" + cardIds +
                ", targetsIdsForCardIds=" + targetsIdsForCardIds +
                '}';
    }
}
