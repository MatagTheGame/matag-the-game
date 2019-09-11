package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.trigger.TriggerType;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Subtype;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.attach.AttachmentsService;
import com.aa.mtg.game.turn.action.selection.AbilitiesFromOtherPermanentsService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aa.mtg.cards.ability.trigger.TriggerType.MANA_ABILITY;
import static com.aa.mtg.cards.ability.type.AbilityType.FLYING;
import static com.aa.mtg.cards.ability.type.AbilityType.REACH;
import static com.aa.mtg.cards.ability.type.AbilityType.VIGILANCE;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.properties.Type.SORCERY;
import static java.util.Collections.emptyList;

@ToString
@EqualsAndHashCode
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Component
@Scope("prototype")
public class CardInstance {

    @JsonProperty private int id;
    @JsonProperty private Card card;
    @JsonProperty private String owner;
    @JsonProperty private String controller;
    @JsonProperty private CardModifiers modifiers = new CardModifiers();
    @JsonProperty private List<Ability> triggeredAbilities = new ArrayList<>();

    private GameStatus gameStatus;
    private final AttachmentsService attachmentsService;
    private final AbilitiesFromOtherPermanentsService abilitiesFromOtherPermanentsService;

    @Autowired
    public CardInstance(
            @Autowired(required = false) AttachmentsService attachmentsService,
            @Autowired(required = false) AbilitiesFromOtherPermanentsService abilitiesFromOtherPermanentsService
    ) {
        this.attachmentsService = attachmentsService;
        this.abilitiesFromOtherPermanentsService = abilitiesFromOtherPermanentsService;
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

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public boolean ofAnyOfTheSubtypes(List<Subtype> subtypes) {
        for (Subtype subtype : subtypes) {
            if (isOfSubtype(subtype)) {
                return true;
            }
        }
        return false;
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
        return card.getPower() +
                modifiers.getExtraPowerToughnessUntilEndOfTurn().getPower() +
                getAttachmentsPower() +
                getPowerFromOtherPermanents();
    }

    @JsonProperty
    public int getToughness() {
        return card.getToughness() +
                modifiers.getExtraPowerToughnessUntilEndOfTurn().getToughness() +
                getAttachmentsToughness() +
                getToughnessFromOtherPermanents();
    }

    @JsonProperty
    public List<Ability> getAbilities() {
        List<Ability> abilities = getStaticAbilities();
        abilities.addAll(getAbilitiesFormOtherPermanents());
        return abilities;
    }

    public List<Ability> getStaticAbilities() {
        List<Ability> abilities = new ArrayList<>();
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

    public boolean hasStaticAbility(AbilityType abilityType) {
        return getStaticAbilities().stream().map(Ability::getAbilityType).anyMatch(currentAbilityType -> currentAbilityType.equals(abilityType));
    }

    public boolean isPermanent() {
        return !(isOfType(INSTANT) || isOfType(SORCERY));
    }

    public boolean isOfSubtype(Subtype subtype) {
        return this.card.getSubtypes().contains(subtype);
    }

    private int getAttachmentsPower() {
        return attachmentsService != null ? attachmentsService.getAttachmentsPower(gameStatus, this) : 0;
    }

    private int getAttachmentsToughness() {
        return attachmentsService != null ? attachmentsService.getAttachmentsToughness(gameStatus, this) : 0;
    }

    private List<Ability> getAttachmentsAbilities() {
        return attachmentsService != null ? attachmentsService.getAttachmentsAbilities(gameStatus, this) : emptyList();
    }

    private int getPowerFromOtherPermanents() {
        return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getPowerFromOtherPermanents(gameStatus, this) : 0;
    }

    private int getToughnessFromOtherPermanents() {
        return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getToughnessFromOtherPermanents(gameStatus, this) : 0;
    }

    private List<Ability> getAbilitiesFormOtherPermanents() {
        return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getAbilitiesFormOtherPermanents(gameStatus, this) : emptyList();
    }

    public void cleanup() {
        modifiers.cleanupUntilEndOfTurnModifiers();
    }

    public void resetAllModifiers() {
        modifiers = new CardModifiers();
    }
}
