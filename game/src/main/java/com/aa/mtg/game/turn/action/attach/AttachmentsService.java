package com.aa.mtg.game.turn.action.attach;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cards.ability.AbilityService;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.PowerToughness;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aa.mtg.cards.ability.Abilities.abilitiesFromParameters;
import static com.aa.mtg.cards.ability.type.AbilityType.ENCHANTED_CREATURE_GETS;
import static com.aa.mtg.cards.ability.type.AbilityType.EQUIPPED_CREATURE_GETS;
import static java.util.Arrays.asList;

@Component
public class AttachmentsService {
    private static final List<AbilityType> ATTACHED_ABILITY_TYPES = asList(ENCHANTED_CREATURE_GETS, EQUIPPED_CREATURE_GETS);

    private AbilityService abilityService;

    public AttachmentsService(AbilityService abilityService) {
        this.abilityService = abilityService;
    }

    public List<CardInstance> getAttachedCards(GameStatus gameStatus, CardInstance cardInstance) {
        return gameStatus.getAllBattlefieldCards().attachedToId(cardInstance.getId()).getCards();
    }

    public int getAttachmentsPower(GameStatus gameStatus, CardInstance cardInstance) {
        return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
                .map(ability -> abilityService.powerToughnessFromParameters(ability.getParameters()))
                .map(PowerToughness::getPower)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public int getAttachmentsToughness(GameStatus gameStatus, CardInstance cardInstance) {
        return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
                .map(ability -> abilityService.powerToughnessFromParameters(ability.getParameters()))
                .map(PowerToughness::getToughness)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public List<CardInstanceAbility> getAttachmentsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
        return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
                .flatMap(ability -> abilitiesFromParameters(ability.getParameters()).stream())
                .collect(Collectors.toList());
    }

    private List<CardInstanceAbility> getAttachedCardsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
        return getAttachedCards(gameStatus, cardInstance).stream()
                .flatMap(attachedCard -> attachedCard.getCard().getAbilities().stream())
                .filter(ability -> ATTACHED_ABILITY_TYPES.contains(ability.getAbilityType()))
                .collect(Collectors.toList());
    }
}
