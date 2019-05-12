package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.FLYING;
import static com.aa.mtg.cards.ability.type.AbilityType.REACH;
import static com.aa.mtg.cards.ability.type.AbilityType.VIGILANCE;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.properties.Type.SORCERY;

@ToString
@EqualsAndHashCode
public class CardInstance {
    private final int id;
    private final Card card;
    private final String owner;
    private String controller;
    private CardModifiers modifiers;

    public CardInstance(int id, Card card, String owner) {
        this(id, card, owner, null);
    }

    public CardInstance(int id, Card card, String owner, String controller) {
        this.id = id;
        this.card = card;
        this.owner = owner;
        this.controller = controller;
        this.modifiers = new CardModifiers();
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

    public static List<CardInstance> mask(List<CardInstance> cardInstances) {
        List<CardInstance> library = new ArrayList<>();
        for (CardInstance cardInstance : cardInstances) {
            library.add(new CardInstance(cardInstance.getId(), Card.hiddenCard(), cardInstance.getOwner()));
        }
        return library;
    }

    public boolean isOfType(Type type) {
        return card.getTypes().contains(type);
    }

    public boolean ofAnyOfTheTypes(List<Type> types) {
        for (Type type : types) {
            if (isOfType(type)) {
                return true;
            }
        }
        return false;
    }

    public void checkIfCanAttack() {
        if (!isOfType(Type.CREATURE)) {
            throw new MessageException("Declared attacker " + getIdAndName() + " is not of type Creature.");
        }

        if (modifiers.isTapped()) {
            throw new MessageException(getIdAndName() + " is already tapped and cannot attack.");
        }

        if (modifiers.isSummoningSickness()) {
            throw new MessageException(getIdAndName() + " is has summoning sickness tapped and cannot attack.");
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
            throw new MessageException("Declared blocker " + getIdAndName() + " is not of type Creature.");
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
        modifiers.addBlocking(attackingCreatureId);
    }

    public int getPower() {
        return card.getPower();
    }

    public int getToughness() {
        return card.getToughness();
    }

    public void clearModifiers() {
        modifiers = new CardModifiers();
    }

    public List<Ability> getAbilities() {
        ArrayList<Ability> abilities = new ArrayList<>();
        abilities.addAll(card.getAbilities());
        abilities.addAll(modifiers.getAbilities());
        abilities.addAll(modifiers.getAbilitiesUntilEndOfTurn());
        return abilities;
    }

    public boolean hasAbility(AbilityType abilityType) {
        return getAbilities().stream().anyMatch(ability -> ability.getAbilityTypes().contains(abilityType));
    }

    public boolean isPermanent() {
        return !(isOfType(INSTANT) || isOfType(SORCERY));
    }
}
