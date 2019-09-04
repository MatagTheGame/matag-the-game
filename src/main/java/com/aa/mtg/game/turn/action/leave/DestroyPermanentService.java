package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DestroyPermanentService {
    private final LeaveBattlefieldService leaveBattlefieldService;
    private final PutInGraveyardService putInGraveyardService;

    @Autowired
    public DestroyPermanentService(LeaveBattlefieldService leaveBattlefieldService, PutInGraveyardService putInGraveyardService) {
        this.leaveBattlefieldService = leaveBattlefieldService;
        this.putInGraveyardService = putInGraveyardService;
    }

    public void destroy(GameStatus gameStatus, int permanentId) {
        CardInstance cardInstance = leaveBattlefieldService.leaveTheBattlefield(gameStatus, permanentId);

        if (cardInstance != null) {
            putInGraveyardService.putInGraveyard(gameStatus, cardInstance);
        }
    }
}
