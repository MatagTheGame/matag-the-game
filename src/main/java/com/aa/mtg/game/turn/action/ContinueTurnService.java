package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContinueTurnService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public ContinueTurnService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void continueTurn(GameStatus gameStatus) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        if (turn.getCurrentPhase().equals(Phase.UT)) {
            currentPlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isTapped())
                    .forEach(cardInstance -> cardInstance.getModifiers().untap());

            currentPlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isSummoningSickness())
                    .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));

            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));

        } else if (turn.getCurrentPhase().equals(Phase.DR)) {
            if (turn.getTurnNumber() > 1) {
                CardInstance cardInstance = currentPlayer.getLibrary().draw();
                currentPlayer.getHand().addCard(cardInstance);
                gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
            }
            turn.setCurrentPhase(Phase.M1);
            turn.setCurrentPhaseActivePlayer(currentPlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.DA)) {
            turn.setCurrentPhase(Phase.DB);
            turn.setCurrentPhaseActivePlayer(nonCurrentPlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.EC)) {
            turn.setCurrentPhase(Phase.M2);
            turn.setCurrentPhaseActivePlayer(currentPlayer.getName());

            // TODO deal damage

            currentPlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isAttacking())
                    .forEach(cardInstance -> cardInstance.getModifiers().setAttacking(false));
            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);

        } else if (turn.getCurrentPhase().equals(Phase.CL)) {
            turn.cleanup(nonCurrentPlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.ET) && currentPlayer.getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");

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
