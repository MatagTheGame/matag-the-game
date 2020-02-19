package com.aa.mtg.cards.ability.trigger;

import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.properties.Cost;

import java.util.List;

import static com.aa.mtg.cards.ability.trigger.TriggerType.*;

public class Trigger {
    private final TriggerType type;
    private final TriggerSubtype subtype;
    private final List<Cost> cost;
    private final CardInstanceSelector cardInstanceSelector;

    private Trigger(TriggerType type, TriggerSubtype subtype, List<Cost> cost, CardInstanceSelector cardInstanceSelector) {
        this.type = type;
        this.subtype = subtype;
        this.cost = cost;
        this.cardInstanceSelector = cardInstanceSelector;
    }

    public TriggerType getType() {
        return type;
    }

    public TriggerSubtype getSubtype() {
        return subtype;
    }

    public List<Cost> getCost() {
        return cost;
    }

    public CardInstanceSelector getCardInstanceSelector() {
        return cardInstanceSelector;
    }

    public static Trigger castTrigger() {
        return new Trigger(CAST, null, null, null);
    }

    public static Trigger manaAbilityTrigger() {
        return new Trigger(MANA_ABILITY, null, null, null);
    }

    public static Trigger triggeredAbility(TriggerSubtype triggerSubtype, CardInstanceSelector cardInstanceSelector) {
        return new Trigger(TRIGGERED_ABILITY, triggerSubtype, null, cardInstanceSelector);
    }

    public static Trigger activatedAbility(List<Cost> cost) {
        return new Trigger(ACTIVATED_ABILITY, null, cost, null);
    }

    public static Trigger staticAbility() {
        return new Trigger(STATIC, null, null, null);
    }
}
