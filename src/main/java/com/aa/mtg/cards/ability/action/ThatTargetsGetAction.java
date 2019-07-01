package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.TRAMPLE;

@Service
public class ThatTargetsGetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        for (Object target : cardInstance.getModifiers().getTargets()) {
            int targetCardId = (int) target;

            String thingsToGet = ability.getParameters().get(1); // FIXME it's wrong to assume that the first parameter is the things that the target will get

            Optional<CardInstance> targetThatIsGettingThingsOptional = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards())
                    .withId(targetCardId);

            if (targetThatIsGettingThingsOptional.isPresent()) {
                CardInstance targetThatIsGettingThings = targetThatIsGettingThingsOptional.get();
                targetThatIsGettingThings.getModifiers().getAbilitiesUntilEndOfTurn().add(TRAMPLE); //FIXME use thing to thingsToGet
                LOGGER.info("AbilityActionExecuted: {} gets {}", cardInstance.getIdAndName(), thingsToGet);

            } else {
                LOGGER.info("target {} is not anymore valid.", targetCardId);
            }
        }
    }
}
