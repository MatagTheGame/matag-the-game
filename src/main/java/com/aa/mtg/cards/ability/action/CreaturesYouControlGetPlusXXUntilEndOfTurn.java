package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreaturesYouControlGetPlusXXUntilEndOfTurn implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreaturesYouControlGetPlusXXUntilEndOfTurn.class);

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        String powerToughness = ability.getParameters().get(0);

        LOGGER.info("creatures you control get: {}", powerToughness);
    }
}
