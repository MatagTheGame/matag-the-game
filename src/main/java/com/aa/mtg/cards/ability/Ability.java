package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.trigger.TriggerSubtype;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.Abilities.parametersAsString;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

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
    @JsonProperty private final List<AbilityType> abilityTypes;
    @JsonProperty private final List<Target> targets;
    @JsonProperty private final List<String> parameters;
    @JsonProperty private final Trigger trigger;

    public Ability(AbilityType abilityType) {
        this(singletonList(abilityType));
    }

    public Ability(List<AbilityType> abilityTypes) {
        this(abilityTypes, emptyList(), emptyList(), null);
    }

    public Ability(AbilityType abilityType, List<Target> targets, List<String> parameters, Trigger trigger) {
        this(singletonList(abilityType), targets, parameters, trigger);
    }

    public Ability(List<AbilityType> abilityTypes, List<Target> targets, List<String> parameters, Trigger trigger) {
        this.abilityTypes = abilityTypes;
        this.targets = targets;
        this.parameters = parameters;
        this.trigger = trigger;
    }

    public AbilityType getFirstAbilityType() {
        return abilityTypes.get(0);
    }

    @JsonProperty
    String getAbilityTypesText() {
        StringBuilder stringBuilder = new StringBuilder();

        Map<AbilityType, List<String>> abilitiesParametersMap = toAbilitiesParametersMap(abilityTypes);
        for (AbilityType abilityType : abilitiesParametersMap.keySet()) {
            String parametersString = parametersAsString(abilitiesParametersMap.get(abilityType));

            boolean negative = parametersString.startsWith("-");
            switch (abilityType) {
                case ADD_X_LIFE:
                    stringBuilder.append(String.format(abilityType.getText(), negative ? "Lose" : "Gain", parametersString.replace("-", "")));
                    break;
                case EACH_PLAYERS_ADD_X_LIFE:
                    stringBuilder.append(String.format(abilityType.getText(), negative ? "loses" : "gains", parametersString.replace("-", "")));
                    break;
                default:
                    stringBuilder.append(String.format(abilityType.getText(), parametersString));
                    break;
            }

            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }

    private Map<AbilityType, List<String>> toAbilitiesParametersMap(List<AbilityType> abilityTypes) {
        Map<AbilityType, List<String>> abilitiesParameters = new LinkedHashMap<>();
        for (int i = 0; i < abilityTypes.size(); i++) {
            AbilityType abilityType = this.abilityTypes.get(i);
            if (!abilitiesParameters.containsKey(abilityType)) {
                abilitiesParameters.put(abilityType, new ArrayList<>());
            }
            abilitiesParameters.get(abilityType).add(getParameter(i));
        }
        return abilitiesParameters;
    }

    public List<AbilityType> getAbilityTypes() {
        return abilityTypes;
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
