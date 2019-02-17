package com.aa.mtg.turn;

public class Turn {
    private int turnNumber;
    private String currentTurnPlayer;
    private String currentPhase;
    private String currentPhaseActivePlayer;

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void setCurrentTurnPlayer(String currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getCurrentPhaseActivePlayer() {
        return currentPhaseActivePlayer;
    }

    public void setCurrentPhaseActivePlayer(String currentPhaseActivePlayer) {
        this.currentPhaseActivePlayer = currentPhaseActivePlayer;
    }
}
