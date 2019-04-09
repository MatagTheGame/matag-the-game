package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.combat.CombatService;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContinueTurnService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final CombatService combatService;

    @Autowired
    public ContinueTurnService(GameStatusUpdaterService gameStatusUpdaterService, CombatService combatService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.combatService = combatService;
    }

    public void continueTurn(GameStatus gameStatus) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        if (!gameStatus.getStack().isEmpty()) {
            CardInstance cardInstance = gameStatus.getStack().removeLast();
            gameStatusUpdaterService.sendUpdateStack(gameStatus);

            cardInstance.getModifiers().setSummoningSickness(true);
            currentPlayer.getBattlefield().addCard(cardInstance);
            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);

            gameStatus.getTurn().setCurrentPhaseActivePlayer(currentPlayer.getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else if (turn.getCurrentPhase().equals(Phase.UT)) {
            currentPlayer.getBattlefield().untap();
            currentPlayer.getBattlefield().removeSummoningSickness();

            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));

        } else if (turn.getCurrentPhase().equals(Phase.DR)) {
            if (turn.getTurnNumber() > 1) {
                CardInstance cardInstance = currentPlayer.getLibrary().draw();
                currentPlayer.getHand().addCard(cardInstance);
                gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
            }
            turn.setCurrentPhase(Phase.M1);

        } else if (turn.getCurrentPhase().equals(Phase.DA)) {
            if (!currentPlayer.getBattlefield().getAttackingCreatures().isEmpty()) {
                turn.setCurrentPhase(Phase.DB);
                turn.setCurrentPhaseActivePlayer(nonCurrentPlayer.getName());
            } else {
                turn.setCurrentPhase(Phase.M2);
            }

        } else if (turn.getCurrentPhase().equals(Phase.DB)) {
            turn.setCurrentPhaseActivePlayer(currentPlayer.getName());
            turn.setCurrentPhase(Phase.FS);

        } else if (turn.getCurrentPhase().equals(Phase.CD)) {
            combatService.dealCombatDamage(gameStatus);

            if (!gameStatus.getTurn().isEnded()) {
                turn.setCurrentPhase(Phase.EC);
                turn.setCurrentPhaseActivePlayer(currentPlayer.getName());
            }

        } else if (turn.getCurrentPhase().equals(Phase.EC)) {
            turn.setCurrentPhase(Phase.M2);
            turn.setCurrentPhaseActivePlayer(currentPlayer.getName());

            currentPlayer.getBattlefield().removeAttacking();
            nonCurrentPlayer.getBattlefield().removeBlocking();
            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
            gameStatusUpdaterService.sendUpdateNonCurrentPlayerBattlefield(gameStatus);

        } else if (turn.getCurrentPhase().equals(Phase.CL)) {
            turn.cleanup(nonCurrentPlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.ET)) {
            if (currentPlayer.getHand().size() > 7) {
                gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");
            }

        } else {
            // TODO possibly this code will be dropped when all phases will be implemented
            if (turn.getCurrentPhaseActivePlayer().equals(currentPlayer.getName())) {
                if (Phase.nonOpponentPhases().contains(turn.getCurrentPhase())) {
                    turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                } else {
                    turn.setCurrentPhaseActivePlayer(nonCurrentPlayer.getName());
                }

            } else {
                turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                turn.setCurrentPhaseActivePlayer(currentPlayer.getName());
            }
        }

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
