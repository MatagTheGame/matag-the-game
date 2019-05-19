package com.aa.mtg.game.turn;

import java.util.List;
import java.util.Map;

public class TurnRequest {
    private String action;
    private String triggeredNonStackAction;
    private List<Integer> tappingLandIds;
    private List<Integer> cardIds;
    private Map<Integer, List<Object>> targetsIdsForCardIds;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTriggeredNonStackAction() {
        return triggeredNonStackAction;
    }

    public void setTriggeredNonStackAction(String triggeredNonStackAction) {
        this.triggeredNonStackAction = triggeredNonStackAction;
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

    public Map<Integer, List<Object>> getTargetsIdsForCardIds() {
        return targetsIdsForCardIds;
    }

    public void setTargetsIdsForCardIds(Map<Integer, List<Object>> targetsIdsForCardIds) {
        this.targetsIdsForCardIds = targetsIdsForCardIds;
    }

    @Override
    public String toString() {
        return "TurnRequest{" +
                "action='" + action + '\'' +
                ", triggeredNonStackAction='" + triggeredNonStackAction + '\'' +
                ", tappingLandIds=" + tappingLandIds +
                ", cardIds=" + cardIds +
                ", targetsIdsForCardIds=" + targetsIdsForCardIds +
                '}';
    }
}
