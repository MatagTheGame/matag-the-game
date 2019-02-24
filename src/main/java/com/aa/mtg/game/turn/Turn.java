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

    public void init(String playerName) {
        this.turnNumber = 1;
        this.currentPhase = Phase.UP;
        this.currentTurnPlayer = playerName;
        this.currentPhaseActivePlayer = playerName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return turnNumber == turn.turnNumber &&
                Objects.equals(currentTurnPlayer, turn.currentTurnPlayer) &&
                currentPhase == turn.currentPhase &&
                Objects.equals(currentPhaseActivePlayer, turn.currentPhaseActivePlayer) &&
                Objects.equals(cardsPlayedWithinTurn, turn.cardsPlayedWithinTurn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnNumber, currentTurnPlayer, currentPhase, currentPhaseActivePlayer, cardsPlayedWithinTurn);
    }
}
