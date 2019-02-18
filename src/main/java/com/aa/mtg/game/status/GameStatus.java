package com.aa.mtg.game.status;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.turn.Turn;

public class GameStatus {

    private Player player1;
    private Player player2;
    private Turn turn;

    public GameStatus() {
        this.turn = new Turn();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
