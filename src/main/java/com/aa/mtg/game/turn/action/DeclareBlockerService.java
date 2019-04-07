package com.aa.mtg.game.turn.action;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
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
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        if (!turn.getCurrentPhase().equals(Phase.DB)) {
            throw new RuntimeException("Blockers declared during phase: " + turn.getCurrentPhase());
        }

        blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) ->
                nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).checkIfCanBlock()
        );

        blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) ->
                nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).declareAsBlocker(blockedCreaturesIds.get(0))
        );

        gameStatusUpdaterService.sendUpdateNonCurrentPlayerBattlefield(gameStatus);
        continueTurnService.continueTurn(gameStatus);
    }
}
