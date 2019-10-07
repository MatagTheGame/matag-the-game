package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.trigger.TriggerSubtype;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.selector.CardInstanceSelector;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.parametersAsString;
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
public class Ability {
    @JsonProperty private final AbilityType abilityType;
    @JsonProperty private final List<Target> targets;
    @JsonProperty private final CardInstanceSelector cardInstanceSelector;
    @JsonProperty private final List<String> parameters;
    @JsonProperty private final Trigger trigger;
    @JsonProperty private final Ability ability;

    public Ability(AbilityType abilityType) {
        this(abilityType, emptyList(), null, emptyList(), null, null);
    }

    public Ability(AbilityType abilityType, List<String> parameters) {
        this(abilityType, emptyList(), null, parameters, null, null);
    }

    public Ability(AbilityType abilityType, List<String> parameters, Ability ability) {
        this(abilityType, emptyList(), null, parameters, null, ability);
    }

    public Ability(AbilityType abilityType, List<Target> targets, List<String> parameters, Trigger trigger) {
        this(abilityType, targets, null, parameters, trigger, null);
    }

    public Ability(AbilityType abilityType, CardInstanceSelector cardInstanceSelector, List<String> parameters, Trigger trigger) {
        this(abilityType, emptyList(), cardInstanceSelector, parameters, trigger, null);
    }

    public Ability(AbilityType abilityType, List<Target> targets, CardInstanceSelector cardInstanceSelector, List<String> parameters, Trigger trigger, Ability ability) {
        this.abilityType = abilityType;
        this.targets = targets;
        this.cardInstanceSelector = cardInstanceSelector;
        this.parameters = parameters;
        this.trigger = trigger;
        this.ability = ability;
    }

    @JsonProperty
    public String getAbilityTypeText() {
        String parametersString = parametersAsString(parameters);

        boolean negative = parametersString.startsWith("-");
        switch (abilityType) {
            case ADD_X_LIFE:
                return String.format(abilityType.getText(), negative ? "Lose" : "Gain", parametersString.replace("-", ""));
            case EACH_PLAYERS_ADD_X_LIFE:
                return String.format(abilityType.getText(), negative ? "loses" : "gains", parametersString.replace("-", ""));
            case SELECTED_PERMANENTS_GET:
                return String.format(abilityType.getText(), cardInstanceSelector.getText(), parametersString) + " until end of turn.";
            default:
                return String.format(abilityType.getText(), parametersString);
        }
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public CardInstanceSelector getCardInstanceSelector() {
        return cardInstanceSelector;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getParameter(int i) {
        if (parameters.size() > i) {
            return parameters.get(i);
        }
        return null;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Ability getAbility() {
        return ability;
    }

    public boolean requiresTarget() {
        return !targets.isEmpty();
    }

    public boolean hasTriggerOfSubtype(TriggerSubtype triggerSubtype) {
        if (trigger != null) {
            return triggerSubtype.equals(trigger.getSubtype());
        }
        return false;
    }
}
