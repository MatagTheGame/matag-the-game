package com.aa.mtg.game.turn.action;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeclareAttackerService {

    private final ContinueTurnService continueTurnService;
    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public DeclareAttackerService(ContinueTurnService continueTurnService, GameStatusUpdaterService gameStatusUpdaterService) {
        this.continueTurnService = continueTurnService;
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();
        Player inactivePlayer = gameStatus.getInactivePlayer();


        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
