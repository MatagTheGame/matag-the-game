package com.aa.mtg.cards.ability.trigger;

import com.aa.mtg.cards.properties.Cost;

import java.util.List;

public class Trigger {
    private final TriggerType type;
    private final TriggerSubtype subtype;
    private final List<Cost> cost;

    public Trigger(TriggerType type) {
        this(type, null, null);
    }

    public Trigger(TriggerType type, TriggerSubtype subtype) {
        this(type, subtype, null);
    }

    public Trigger(TriggerType type, List<Cost> cost) {
        this(type, null, cost);
    }

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
}
