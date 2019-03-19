package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
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
public class DeclareBlockerService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final ContinueTurnService continueTurnService;

    @Autowired
    public DeclareBlockerService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
    }

    public void declareBlockers(GameStatus gameStatus, List<Integer> cardIds) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        if (!turn.getCurrentPhase().equals(Phase.DB)) {
            throw new RuntimeException("Blockers declared during phase: " + turn.getCurrentPhase());
        }

        try {
            CardInstance firstAttackingCreature = currentPlayer.getBattlefield().getAttackingCreatures().get(0);
            // TODO Currently all blocking creatures are blocking the first creature
            cardIds.forEach(cardId -> nonCurrentPlayer.getBattlefield().findCardById(cardId).declareAsBlocker(firstAttackingCreature.getId()));
        } catch (MessageException e) {
            gameStatusUpdaterService.sendMessageToCurrentPlayer(nonCurrentPlayer, e.getMessage());
        }

        gameStatusUpdaterService.sendUpdateNonCurrentPlayerBattlefield(gameStatus);
        continueTurnService.continueTurn(gameStatus);
    }
}
