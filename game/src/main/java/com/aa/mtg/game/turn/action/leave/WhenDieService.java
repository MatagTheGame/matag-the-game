package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cardinstance.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.selection.CardInstanceSelectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_DIE;

@Component
public class WhenDieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhenDieService.class);

    private final CardInstanceSelectorService cardInstanceSelectorService;

    @Autowired
    public WhenDieService(CardInstanceSelectorService cardInstanceSelectorService) {
        this.cardInstanceSelectorService = cardInstanceSelectorService;
    }

    void whenDie(GameStatus gameStatus, CardInstance cardInstance) {
        List<CardInstance> cardsWithDieAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(WHEN_DIE).getCards();

        for (CardInstance cardWithDieAbility : cardsWithDieAbility) {
            for (Ability ability : cardWithDieAbility.getAbilitiesByTriggerSubType(WHEN_DIE)) {
                CardInstanceSearch selectionForCardWithEnterAbility = cardInstanceSelectorService.select(gameStatus, cardWithDieAbility, ability.getTrigger().getCardInstanceSelector());
                if (selectionForCardWithEnterAbility.withId(cardInstance.getId()).isPresent()) {
                    cardWithDieAbility.getTriggeredAbilities().add(ability);
                }
            }

            if (!cardWithDieAbility.getTriggeredAbilities().isEmpty()) {
                LOGGER.info("{} triggered {} because of {} dying.", cardInstance.getIdAndName(), WHEN_DIE, cardInstance.getIdAndName());
                gameStatus.getStack().add(cardWithDieAbility);
            }
        }
    }
}
