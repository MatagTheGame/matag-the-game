package com.matag.game.turn;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Component
@Scope("prototype")
public class Turn {
  private int turnNumber;
  private String currentTurnPlayer;
  private String currentPhase;
  private String currentPhaseActivePlayer;
  private List<CardInstance> cardsPlayedWithinTurn = new ArrayList<>();
  private Map<Integer, List<String>> lastManaPaid = new HashMap<>();
  private String inputRequiredAction;
  private String inputRequiredActionParameter;
  private String winner;

  public void init(String playerName) {
    this.turnNumber = 1;
    this.currentPhase = "UP";
    this.currentTurnPlayer = playerName;
    this.currentPhaseActivePlayer = playerName;
  }

  public void increaseTurnNumber() {
    turnNumber++;
  }

  public void addCardToCardsPlayedWithinTurn(CardInstance cardInstance) {
    cardsPlayedWithinTurn.add(cardInstance);
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