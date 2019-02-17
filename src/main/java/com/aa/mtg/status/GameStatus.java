package com.aa.mtg.status;

import com.aa.mtg.player.Player;
import com.aa.mtg.turn.Turn;

public class GameStatus {

    private Player player1;
    private Player player2;
    private Turn turn;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
