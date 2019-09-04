package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReturnPermanentToHandService {
    private final LeaveBattlefieldService leaveBattlefieldService;
    private final PutIntoHandService returnToHand;

    @Autowired
    public ReturnPermanentToHandService(LeaveBattlefieldService leaveBattlefieldService, PutIntoHandService returnToHand) {
        this.leaveBattlefieldService = leaveBattlefieldService;
        this.returnToHand = returnToHand;
    }

    public void returnPermanentToHand(GameStatus gameStatus, int targetId) {
        CardInstance cardInstance = leaveBattlefieldService.leaveTheBattlefield(gameStatus, targetId);

        if (cardInstance != null) {
            returnToHand.returnToHand(gameStatus, cardInstance);
        }
    }
}
