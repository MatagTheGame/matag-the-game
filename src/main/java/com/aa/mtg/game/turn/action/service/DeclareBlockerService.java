package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;

@Component
public class DeclareBlockerService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final ContinueTurnService continueTurnService;

    @Autowired
    public DeclareBlockerService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
    }

    public void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        if (!turn.getCurrentPhase().equals(DB)) {
            throw new RuntimeException("Blockers declared during phase: " + turn.getCurrentPhase());
        }

        blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
            CardInstance blockedCreature = currentPlayer.getBattlefield().findCardById(blockedCreaturesIds.get(0));
            nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).checkIfCanBlock(blockedCreature);
        });

        blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) ->
                nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).declareAsBlocker(blockedCreaturesIds.get(0))
        );

        gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, nonCurrentPlayer);
        continueTurnService.continueTurn(gameStatus);
    }
}
