package com.aa.mtg.game.turn.action;

import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeclareAttackerService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public DeclareAttackerService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();

        if (!turn.getCurrentPhase().equals(Phase.DA)) {
            throw new RuntimeException("Attackers declared during phase: " + turn.getCurrentPhase());
        }

        try {
            cardIds.forEach(cardId -> currentPlayer.getBattlefield().findCardById(cardId).declareAsAttacker());
        } catch (MessageException e) {
            gameStatusUpdaterService.sendMessageToCurrentPlayer(currentPlayer, e.getMessage());
        }

        gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);

        turn.setCurrentPhase(Phase.DB);
        turn.setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
