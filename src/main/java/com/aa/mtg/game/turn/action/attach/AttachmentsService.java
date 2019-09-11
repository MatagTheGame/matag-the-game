package com.aa.mtg.game.turn.action.attach;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.abilitiesFromParameters;
import static com.aa.mtg.cards.ability.Abilities.powerToughnessFromParameters;
import static com.aa.mtg.cards.ability.type.AbilityType.ENCHANTED_CREATURE_GETS;
import static com.aa.mtg.cards.ability.type.AbilityType.EQUIPPED_CREATURE_GETS;

@Component
public class AttachmentsService {
    public List<CardInstance> getAttachedCards(GameStatus gameStatus, CardInstance cardInstance) {
        return gameStatus.getAllBattlefieldCards().attachedToId(cardInstance.getId()).getCards();
    }

    public int getAttachmentsPower(GameStatus gameStatus, CardInstance cardInstance) {
        int attachmentsPower = 0;
        for (Ability ability : getAttachedCardsAbilities(gameStatus, cardInstance)) {
            attachmentsPower += powerToughnessFromParameters(ability.getParameters()).getPower();
        }

        return attachmentsPower;
    }

    public int getAttachmentsToughness(GameStatus gameStatus, CardInstance cardInstance) {
        int attachmentsToughness = 0;
        for (Ability ability : getAttachedCardsAbilities(gameStatus, cardInstance)) {
            attachmentsToughness += powerToughnessFromParameters(ability.getParameters()).getToughness();
        }

        return attachmentsToughness;
    }

    public List<Ability> getAttachmentsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability : getAttachedCardsAbilities(gameStatus, cardInstance)) {
            abilities.addAll(abilitiesFromParameters(ability.getParameters()));
        }
        return abilities;
    }

    private List<Ability> getAttachedCardsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
        List<Ability> abilities = new ArrayList<>();
        for (CardInstance attachedCards : getAttachedCards(gameStatus, cardInstance)) {
            for (Ability ability : attachedCards.getAbilities()) {
                if (ability.getAbilityType().equals(ENCHANTED_CREATURE_GETS) || ability.getAbilityType().equals(EQUIPPED_CREATURE_GETS)) {
                    abilities.add(ability);
                }
            }
        }
        return abilities;
    }
}
