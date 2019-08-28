package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.trigger.TriggerType;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CardInstance {
    @JsonIgnore private final GameStatus gameStatus;
    private final int id;
    private final Card card;
    private final String owner;
    private String controller;
    private CardModifiers modifiers;
    private List<Ability> triggeredAbilities = new ArrayList<>();

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

    public int getPower() {
        return card.getPower() + modifiers.getExtraPowerToughnessUntilEndOfTurn().getPower() + getAttachmentsPower();
    }

    public int getToughness() {
        return card.getToughness() + modifiers.getExtraPowerToughnessUntilEndOfTurn().getToughness() + getAttachmentsToughness();
    }

    public void clearModifiers() {
        modifiers = new CardModifiers();
    }

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

    @JsonIgnore
    public List<Ability> getAbilitiesByTriggerType(TriggerType triggerType) {
        return getAbilities().stream()
                .filter(ability -> ability.getTrigger() != null)
                .filter(ability -> ability.getTrigger().getType().equals(triggerType))
                .collect(Collectors.toList());
    }

    public boolean hasAbility(AbilityType abilityType) {
        return getAbilities().stream().anyMatch(ability -> ability.getAbilityTypes().contains(abilityType));
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
                if (ability.getAbilityTypes().contains(ENCHANTED_CREATURE_GETS) || ability.getAbilityTypes().contains(EQUIPPED_CREATURE_GETS)) {
                    abilities.add(ability);
                }
            }
        }
        return abilities;
    }

    public void cleanup() {
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
