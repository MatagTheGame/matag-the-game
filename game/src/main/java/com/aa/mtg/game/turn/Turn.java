package com.aa.mtg.game.turn;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Component
@Scope("prototype")
public class Turn {
  private int turnNumber;
  private String currentTurnPlayer;
  private String currentPhase;
  private String currentPhaseActivePlayer;
  private List<CardInstance> cardsPlayedWithinTurn = new ArrayList<>();
  private Map<Integer, List<String>> lastManaPaid = new HashMap<>();
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

  public void setCardsPlayedWithinTurn(List<CardInstance> cardsPlayedWithinTurn) {
    this.cardsPlayedWithinTurn = cardsPlayedWithinTurn;
  }

  public Map<Integer, List<String>> getLastManaPaid() {
    return lastManaPaid;
  }

  public void setLastManaPaid(Map<Integer, List<String>> mana) {
    this.lastManaPaid = mana;
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

  public void passPriority(GameStatus gameStatus) {
    if (currentPhaseActivePlayer.equals(gameStatus.getPlayer1().getName())) {
      currentPhaseActivePlayer = gameStatus.getPlayer2().getName();
    } else {
      currentPhaseActivePlayer = gameStatus.getPlayer1().getName();
    }
  }
}
