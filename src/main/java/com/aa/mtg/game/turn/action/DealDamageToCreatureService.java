package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealDamageToCreatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToCreatureService.class);

    private final DestroyTargetService destroyTargetService;

    @Autowired
    public DealDamageToCreatureService(DestroyTargetService destroyTargetService) {
        this.destroyTargetService = destroyTargetService;
    }

    public void dealDamageToCreature(GameStatus gameStatus, CardInstance cardInstance, int damage, boolean deathtouch) {
        if (damage > 0) {
            LOGGER.info("{} is getting {} damage.", cardInstance.getIdAndName(), damage);
            cardInstance.getModifiers().dealDamage(damage);
            if (cardInstance.getModifiers().getDamage() >= cardInstance.getToughness() || deathtouch) {
                destroyTargetService.destroy(gameStatus, cardInstance.getId());
                LOGGER.info("{} has been destroyed.", cardInstance.getIdAndName());
            }
        }
    }
}
