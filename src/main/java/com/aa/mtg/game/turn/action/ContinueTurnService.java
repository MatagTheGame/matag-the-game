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
        Player activePlayer = gameStatus.getActivePlayer();
        Player inactivePlayer = gameStatus.getInactivePlayer();

        if (turn.getCurrentPhase().equals(Phase.UT)) {
            activePlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isTapped())
                    .forEach(cardInstance -> cardInstance.getModifiers().untap());

            activePlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isSummoningSickness())
                    .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));

            gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));

        } else if (turn.getCurrentPhase().equals(Phase.CL)) {
            turn.cleanup(inactivePlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.ET) && activePlayer.getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");

        } else {
            if (turn.getCurrentPhaseActivePlayer().equals(activePlayer.getName())) {
                if (turn.getCurrentPhase().equals(Phase.DR) && turn.getTurnNumber() > 1) {
                    CardInstance cardInstance = activePlayer.getLibrary().draw();
                    activePlayer.getHand().addCard(cardInstance);
                    gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
                }

                if (Phase.nonOpponentPhases().contains(turn.getCurrentPhase())) {
                    turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                } else {
                    turn.setCurrentPhaseActivePlayer(inactivePlayer.getName());
                }

            } else {
                turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                turn.setCurrentPhaseActivePlayer(activePlayer.getName());
            }
        }

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
