package com.aa.mtg.game.turn.action._continue;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.DestroyPermanentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class ConsolidateStatusService {
    private final DestroyPermanentService destroyPermanentService;

    @Autowired
    public ConsolidateStatusService(DestroyPermanentService destroyPermanentService) {
        this.destroyPermanentService = destroyPermanentService;
    }

    public void consolidate(GameStatus gameStatus) {
        boolean repeat;
        do {
            repeat = false;
            for (CardInstance card : gameStatus.getAllBattlefieldCards().getCards()) {
                if (card.isOfType(CREATURE) && card.getToughness() - card.getModifiers().getDamage() <= 0) {
                    destroyPermanentService.destroy(gameStatus, card.getId());
                    repeat = true;
                }
            }
        } while (repeat);
    }
}
