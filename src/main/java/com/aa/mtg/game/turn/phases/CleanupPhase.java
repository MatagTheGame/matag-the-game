package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.UntapPhase.UT;

@Component
public class CleanupPhase implements Phase {
    public static final String CL = "CL";

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public CleanupPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        cleanup(gameStatus);
        moveToNextPlayer(gameStatus);
    }

    private void cleanup(GameStatus gameStatus) {
        gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
        gameStatus.getCurrentPlayer().getBattlefield().getCards().forEach(CardInstance::cleanup);
        gameStatus.getNonCurrentPlayer().getBattlefield().getCards().forEach(CardInstance::cleanup);
        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
    }

    private void moveToNextPlayer(GameStatus gameStatus) {
        gameStatus.getTurn().increaseTurnNumber();
        gameStatus.getTurn().setCurrentPhase(UT);
        String nextCurrentPlayer = gameStatus.getNonCurrentPlayer().getName();
        gameStatus.getTurn().setCurrentTurnPlayer(nextCurrentPlayer);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(nextCurrentPlayer);
    }
}
