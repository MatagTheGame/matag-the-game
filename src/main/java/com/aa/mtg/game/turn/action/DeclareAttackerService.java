package com.aa.mtg.game.turn.action;

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
    private final ContinueTurnService continueTurnService;

    @Autowired
    public DeclareAttackerService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
    }

    public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();

        if (!turn.getCurrentPhase().equals(Phase.DA)) {
            throw new RuntimeException("Attackers declared during phase: " + turn.getCurrentPhase());
        }

        cardIds.forEach(cardId -> currentPlayer.getBattlefield().findCardById(cardId).checkIfCanAttack());
        cardIds.forEach(cardId -> currentPlayer.getBattlefield().findCardById(cardId).declareAsAttacker());

        gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
        continueTurnService.continueTurn(gameStatus);
    }
}
