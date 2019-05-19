package com.aa.mtg.game.turn;

import com.aa.mtg.cards.CardInstance;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Turn {
    private int turnNumber;
    private String currentTurnPlayer;
    private String currentPhase;
    private String currentPhaseActivePlayer;
    private List<CardInstance> cardsPlayedWithinTurn = new ArrayList<>();
    private String triggeredNonStackAction;
    private String winner;

    public void init(String playerName) {
        this.turnNumber = 1;
        this.currentPhase = "UP";
        this.currentTurnPlayer = playerName;
        this.currentPhaseActivePlayer = playerName;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void increaseTurnNumber() {
        turnNumber++;
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

    public List<CardInstance> getCardsPlayedWithinTurn() {
        return cardsPlayedWithinTurn;
    }

    public void addCardToCardsPlayedWithinTurn(CardInstance cardInstance) {
        cardsPlayedWithinTurn.add(cardInstance);
    }

    public String getTriggeredNonStackAction() {
        return triggeredNonStackAction;
    }

    public void setTriggeredNonStackAction(String triggeredNonStackAction) {
        this.triggeredNonStackAction = triggeredNonStackAction;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isEnded() {
        return winner != null;
    }
}
