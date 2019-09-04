package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.trigger.TriggerType;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aa.mtg.cards.ability.Abilities.abilitiesFromParameters;
import static com.aa.mtg.cards.ability.Abilities.powerToughnessFromParameters;
import static com.aa.mtg.cards.ability.trigger.TriggerType.MANA_ABILITY;
import static com.aa.mtg.cards.ability.type.AbilityType.*;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.properties.Type.SORCERY;

@ToString
@EqualsAndHashCode
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public class CardInstance {
    private final GameStatus gameStatus;
    @JsonProperty private final int id;
    @JsonProperty private final Card card;
    @JsonProperty private final String owner;
    @JsonProperty private String controller;
    @JsonProperty private CardModifiers modifiers;
    @JsonProperty private List<Ability> triggeredAbilities = new ArrayList<>();

    public CardInstance(GameStatus gameStatus, int id, Card card, String owner) {
        this(gameStatus, id, card, owner, null);
    }

    public CardInstance(GameStatus gameStatus, int id, Card card, String owner, String controller) {
        this.gameStatus = gameStatus;
        this.id = id;
        this.card = card;
        this.owner = owner;
        this.controller = controller;
        this.modifiers = new CardModifiers();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getId() {
        return id;
    }

    public String getIdAndName() {
        return "\"" + id + " - " + card.getName() + "\"";
    }

    public Card getCard() {
        return card;
    }

    public String getOwner() {
        return owner;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public CardModifiers getModifiers() {
        return modifiers;
    }

    public List<Ability> getTriggeredAbilities() {
        return triggeredAbilities;
    }

    public boolean isOfType(Type type) {
        return card.isOfType(type);
    }

    public boolean ofAnyOfTheTypes(List<Type> types) {
        for (Type type : types) {
            if (isOfType(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean ofAllOfTheTypes(List<Type> types) {
        for (Type type : types) {
            if (!isOfType(type)) {
                return false;
            }
        }
        return true;
    }

    public void checkIfCanAttack() {
        if (!isOfType(Type.CREATURE)) {
            throw new MessageException(getIdAndName() + " is not of type Creature.");
        }

        if (modifiers.isTapped()) {
            throw new MessageException(getIdAndName() + " is already tapped and cannot attack.");
        }

        if (modifiers.isSummoningSickness()) {
            throw new MessageException(getIdAndName() + " has summoning sickness and cannot attack.");
        }
    }

    public void declareAsAttacker() {
        if (!hasAbility(VIGILANCE)) {
            modifiers.tap();
        }
        modifiers.setAttacking(true);
    }

    public void checkIfCanBlock(CardInstance blockedCreature) {
        if (!isOfType(Type.CREATURE)) {
            throw new MessageException(getIdAndName() + " is not of type Creature.");
        }

        if (modifiers.isTapped()) {
            throw new MessageException(getIdAndName() + " is tapped and cannot block.");
        }

        if (blockedCreature.hasAbility(FLYING)) {
            if (!(hasAbility(FLYING) || hasAbility(REACH))) {
                throw new MessageException(getIdAndName() + " cannot block " + blockedCreature.getIdAndName() + " as it has flying.");
            }
        }
    }

    public void declareAsBlocker(int attackingCreatureId) {
        modifiers.setBlockingCardId(attackingCreatureId);
    }

    @JsonProperty
    public int getPower() {
        return card.getPower() + modifiers.getExtraPowerToughnessUntilEndOfTurn().getPower() + getAttachmentsPower();
    }

    @JsonProperty
    public int getToughness() {
        return card.getToughness() + modifiers.getExtraPowerToughnessUntilEndOfTurn().getToughness() + getAttachmentsToughness();
    }

    @JsonProperty
    public List<Ability> getAbilities() {
        ArrayList<Ability> abilities = new ArrayList<>();
        abilities.addAll(card.getAbilities());
        abilities.addAll(modifiers.getAbilities());
        abilities.addAll(modifiers.getAbilitiesUntilEndOfTurn());
        abilities.addAll(getAttachmentsAbilities());
        return abilities;
    }

    public boolean canProduceMana(Color color) {
        return getAbilitiesByTriggerType(MANA_ABILITY).stream()
                .flatMap(ability -> ability.getParameters().stream())
                .anyMatch(parameter -> parameter.equals(color.toString()));
    }

    public List<Ability> getAbilitiesByTriggerType(TriggerType triggerType) {
        return getAbilities().stream()
                .filter(ability -> ability.getTrigger() != null)
                .filter(ability -> ability.getTrigger().getType().equals(triggerType))
                .collect(Collectors.toList());
    }

    public boolean hasAbility(AbilityType abilityType) {
        return getAbilities().stream().map(Ability::getAbilityType).anyMatch(currentAbilityType -> currentAbilityType.equals(abilityType));
    }

    public boolean isPermanent() {
        return !(isOfType(INSTANT) || isOfType(SORCERY));
    }

    public boolean isOfSubtype(String subtype) {
        return this.card.getSubtypes().contains(subtype);
    }

    public List<CardInstance> getAttachedCards() {
        return gameStatus.getAllBattlefieldCards().attachedToId(this.id).getCards();
    }

    private int getAttachmentsPower() {
        int attachmentsPower = 0;
        for (Ability ability : getAttachedCardsAbilities()) {
            attachmentsPower += powerToughnessFromParameters(ability.getParameters()).getPower();
        }

        return attachmentsPower;
    }

    private int getAttachmentsToughness() {
        int attachmentsToughness = 0;
        for (Ability ability : getAttachedCardsAbilities()) {
            attachmentsToughness += powerToughnessFromParameters(ability.getParameters()).getToughness();
        }

        return attachmentsToughness;
    }

    private List<Ability> getAttachmentsAbilities() {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability : getAttachedCardsAbilities()) {
            abilities.addAll(abilitiesFromParameters(ability.getParameters()));
        }
        return abilities;
    }

    private List<Ability> getAttachedCardsAbilities() {
        List<Ability> abilities = new ArrayList<>();
        for (CardInstance attachedCards : getAttachedCards()) {
            for (Ability ability : attachedCards.getAbilities()) {
                if (ability.getAbilityType().equals(ENCHANTED_CREATURE_GETS) || ability.getAbilityType().equals(EQUIPPED_CREATURE_GETS)) {
                    abilities.add(ability);
                }
            }
        }
        return abilities;
    }

    public void cleanup() {
        modifiers.cleanupUntilEndOfTurnModifiers();
    }

    public void resetAllModifiers() {
        modifiers = new CardModifiers();
    }

    public static List<CardInstance> mask(List<CardInstance> cardInstances) {
        List<CardInstance> library = new ArrayList<>();
        for (CardInstance cardInstance : cardInstances) {
            library.add(new CardInstance(cardInstance.getGameStatus(), cardInstance.getId(), Card.hiddenCard(), cardInstance.getOwner()));
        }
        return library;
    }
}
