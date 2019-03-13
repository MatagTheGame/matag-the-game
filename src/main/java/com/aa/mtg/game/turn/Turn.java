package com.aa.mtg.game.turn;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.turn.phases.Phase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turn {
    private int turnNumber;
    private String currentTurnPlayer;
    private Phase currentPhase;
    private String currentPhaseActivePlayer;
    private List<CardInstance> cardsPlayedWithinTurn = new ArrayList<>();
    private String triggeredAction;
    private String winner;

    public void init(String playerName) {
        this.turnNumber = 1;
        this.currentPhase = Phase.UP;
        this.currentTurnPlayer = playerName;
        this.currentPhaseActivePlayer = playerName;
    }

    public void cleanup(String nextPlayerName) {
        this.turnNumber++;
        this.currentPhase = Phase.UT;
        this.currentTurnPlayer = nextPlayerName;
        this.currentPhaseActivePlayer = nextPlayerName;
        this.cardsPlayedWithinTurn.clear();
    }

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

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
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

    public String getTriggeredAction() {
        return triggeredAction;
    }

    public void setTriggeredAction(String triggeredAction) {
        this.triggeredAction = triggeredAction;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return turnNumber == turn.turnNumber &&
                Objects.equals(currentTurnPlayer, turn.currentTurnPlayer) &&
                currentPhase == turn.currentPhase &&
                Objects.equals(currentPhaseActivePlayer, turn.currentPhaseActivePlayer) &&
                Objects.equals(cardsPlayedWithinTurn, turn.cardsPlayedWithinTurn) &&
                Objects.equals(triggeredAction, turn.triggeredAction) &&
                Objects.equals(winner, turn.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnNumber, currentTurnPlayer, currentPhase, currentPhaseActivePlayer, cardsPlayedWithinTurn, triggeredAction, winner);
    }

    @Override
    public String toString() {
        return "Turn{" +
                "turnNumber=" + turnNumber +
                ", currentTurnPlayer='" + currentTurnPlayer + '\'' +
                ", currentPhase=" + currentPhase +
                ", currentPhaseActivePlayer='" + currentPhaseActivePlayer + '\'' +
                ", cardsPlayedWithinTurn=" + cardsPlayedWithinTurn +
                ", triggeredAction='" + triggeredAction + '\'' +
                ", winner='" + winner + '\'' +
                '}';
    }
}
