package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DestroyPermanentService {
    private final LeaveBattlefieldService leaveBattlefieldService;
    private final PutIntoGraveyardService putIntoGraveyardService;
    private final WhenDieService whenDieService;

    @Autowired
    public DestroyPermanentService(LeaveBattlefieldService leaveBattlefieldService, PutIntoGraveyardService putIntoGraveyardService, WhenDieService whenDieService) {
        this.leaveBattlefieldService = leaveBattlefieldService;
        this.putIntoGraveyardService = putIntoGraveyardService;
        this.whenDieService = whenDieService;
    }

    public void destroy(GameStatus gameStatus, int permanentId) {
        CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId);

        if (cardInstance != null) {
            whenDieService.whenDie(gameStatus, cardInstance);
            leaveBattlefieldService.leaveTheBattlefield(gameStatus, permanentId);
            putIntoGraveyardService.putIntoGraveyard(gameStatus, cardInstance);
        }
    }
}
