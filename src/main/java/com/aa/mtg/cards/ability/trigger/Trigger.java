package com.aa.mtg.cards.ability.trigger;

import com.aa.mtg.cards.properties.Cost;

import java.util.List;

import static com.aa.mtg.cards.ability.trigger.TriggerType.ACTIVATED_ABILITY;
import static com.aa.mtg.cards.ability.trigger.TriggerType.CAST;
import static com.aa.mtg.cards.ability.trigger.TriggerType.MANA_ABILITY;
import static com.aa.mtg.cards.ability.trigger.TriggerType.TRIGGERED_ABILITY;

public class Trigger {
    private final TriggerType type;
    private final TriggerSubtype subtype;
    private final List<Cost> cost;

    private Trigger(TriggerType type, TriggerSubtype subtype, List<Cost> cost) {
        this.type = type;
        this.subtype = subtype;
        this.cost = cost;
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

    public static Trigger castTrigger() {
        return new Trigger(CAST, null, null);
    }

    public static Trigger manaAbilityTrigger() {
        return new Trigger(MANA_ABILITY, null, null);
    }

    public static Trigger triggeredAbility(TriggerSubtype triggerSubtype) {
        return new Trigger(TRIGGERED_ABILITY, triggerSubtype, null);
    }

    public static Trigger activatedAbility(List<Cost> cost) {
        return new Trigger(ACTIVATED_ABILITY, null, cost);
    }
}
