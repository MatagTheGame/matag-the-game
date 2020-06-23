package com.matag.game.status;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.player.Player;
import com.matag.game.stack.SpellStack;
import com.matag.game.turn.Turn;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
@Scope("prototype")
public class GameStatus {
  private final AtomicInteger nextCardId = new AtomicInteger();
  private String gameId;
  private Player player1;
  private Player player2;
  private Turn turn;
  private SpellStack stack;

  public GameStatus(Turn turn, SpellStack stack) {
    this.turn = turn;
    this.stack = stack;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public void setPlayer1(Player player1) {
    this.player1 = player1;
  }

  public void setPlayer2(Player player2) {
    this.player2 = player2;
  }

  public Player getPlayerByName(String name) {
    if (this.getPlayer1().getName().equals(name)) {
      return this.getPlayer1();
    } else {
      return this.getPlayer2();
    }
  }

  public Player getOtherPlayer(Player player) {
    return getOtherPlayer(player.getName());
  }

  public Player getOtherPlayer(String playerName) {
    if (player1.getName().equals(playerName)) {
      return player2;
    } else {
      return player1;
    }
  }

  public Player getCurrentPlayer() {
    if (turn.getCurrentTurnPlayer().equals(this.getPlayer1().getName())) {
      return this.getPlayer1();
    } else {
      return this.getPlayer2();
    }
  }

  public Player getNonCurrentPlayer() {
    return getOtherPlayer(getCurrentPlayer());
  }

  public Player getActivePlayer() {
    if (turn.getCurrentPhaseActivePlayer().equals(this.getPlayer1().getName())) {
      return this.getPlayer1();
    } else {
      return this.getPlayer2();
    }
  }

  public Player getNonActivePlayer() {
    return getOtherPlayer(getActivePlayer());
  }

  public int nextCardId() {
    return nextCardId.incrementAndGet();
  }

  public CardInstance extractCardByIdFromAnyBattlefield(int id) {
    if (getNonCurrentPlayer().getBattlefield().hasCardById(id)) {
      return getNonCurrentPlayer().getBattlefield().extractCardById(id);

    } else if (getCurrentPlayer().getBattlefield().hasCardById(id)) {
      return getCurrentPlayer().getBattlefield().extractCardById(id);
    }

    return null;
  }

  public CardInstance findCardByIdFromAnyBattlefield(int id) {
    if (getNonCurrentPlayer().getBattlefield().hasCardById(id)) {
      return getNonCurrentPlayer().getBattlefield().findCardById(id);

    } else if (getCurrentPlayer().getBattlefield().hasCardById(id)) {
      return getCurrentPlayer().getBattlefield().findCardById(id);
    }

    return null;
  }

  public CardInstanceSearch getAllBattlefieldCards() {
    return new CardInstanceSearch(player1.getBattlefield().getCards())
      .concat(player2.getBattlefield().getCards());
  }
}
