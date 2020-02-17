package com.aa.mtg.game.turn.action.combat;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.action._continue.ContinueTurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;

@Component
public class DeclareBlockerService {

    private final ContinueTurnService continueTurnService;

    @Autowired
    public DeclareBlockerService(ContinueTurnService continueTurnService) {
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

        continueTurnService.continueTurn(gameStatus);
    }
}
