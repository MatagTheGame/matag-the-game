package com.aa.mtg.game.turn;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnService {

    private final EventSender eventSender;

    @Autowired
    public TurnService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    public void continueTurn(GameStatus gameStatus) {
        Turn turn = gameStatus.getTurn();

        Player player, opponent;

        if (turn.getCurrentTurnPlayer().equals(gameStatus.getPlayer1().getName())) {
            player = gameStatus.getPlayer1();
            opponent = gameStatus.getPlayer2();

        } else {
            player = gameStatus.getPlayer2();
            opponent = gameStatus.getPlayer1();
        }

        updateToNextTurn(turn, player, opponent);

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("UPDATE_TURN", gameStatus.getTurn()));
        eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("UPDATE_TURN", gameStatus.getTurn()));
    }

    private void updateToNextTurn(Turn turn, Player player, Player opponent) {
        if (turn.getCurrentPhaseActivePlayer().equals(player.getName())) {
            if (Phase.nonOpponentPhases().contains(turn.getCurrentPhase())) {
                turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
            } else {
                turn.setCurrentPhaseActivePlayer(opponent.getName());
            }

        } else {
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
            turn.setCurrentPhaseActivePlayer(player.getName());
        }
    }
}
