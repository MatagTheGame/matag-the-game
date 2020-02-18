package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.properties.PowerToughness;

import java.util.List;
import java.util.stream.Collectors;

import static com.aa.mtg.cards.properties.PowerToughness.powerToughness;
import static com.aa.mtg.utils.Utils.replaceLast;
import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;

public class AbilityUtils {
    public static PowerToughness powerToughnessFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> parameter.contains("/"))
                .map(PowerToughness::powerToughness)
                .findFirst()
                .orElse(powerToughness("0/0"));
    }

    public static PowerToughness powerToughnessFromParameter(String parameter) {
        return powerToughnessFromParameters(singletonList(parameter));
    }

    public static int damageFromParameter(String parameter) {
        if (parameter.startsWith("DAMAGE:")) {
            return parseInt(parameter.replace("DAMAGE:", ""));
        }
        return 0;
    }

    public static int lifeFromParameter(String parameter) {
        if (parameter.startsWith("LIFE:")) {
            return parseInt(parameter.replace("LIFE:", ""));
        }
        return 0;
    }

    public static int controllerDamageFromParameter(String parameter) {
        if (parameter.startsWith("CONTROLLER_DAMAGE:")) {
            return parseInt(parameter.replace("CONTROLLER_DAMAGE:", ""));
        }
        return 0;
    }

    public static boolean destroyedFromParameter(String parameter) {
        return parameter.equals(":DESTROYED");
    }

    public static boolean tappedFromParameter(String parameter) {
        return parameter.equals(":TAPPED");
    }

    public static boolean tappedDoesNotUntapNextTurnFromParameter(String parameter) {
        return parameter.equals(":TAPPED_DOES_NOT_UNTAP_NEXT_TURN");
    }

    public static boolean returnToOwnerHandFromParameter(String parameter) {
        return parameter.equals(":RETURN_TO_OWNER_HAND");
    }

    public static boolean untappedFromParameter(String parameter) {
        return parameter.equals(":UNTAPPED");
    }

    public static int drawFromParameter(String parameter) {
        if (parameter.startsWith("DRAW:")) {
            return parseInt(parameter.replace("DRAW:", ""));
        }
        return 0;
    }

    public static boolean controlledFromParameter(String parameter) {
        return parameter.equals(":CONTROLLED");
    }

    public static int plus1CountersFromParameter(String parameter) {
        if (parameter.startsWith("PLUS_1_COUNTERS:")) {
            return parseInt(parameter.replace("PLUS_1_COUNTERS:", ""));
        }
        return 0;
    }

    public static String parametersAsString(List<String> parameters) {
        String text = parameters.stream().map(AbilityUtils::parameterAsString).collect(Collectors.joining(", "));
        return replaceLast(text, ",", " and");
    }

    private static String parameterAsString(String parameter) {
        if (parameter == null) {
            return null;
        } else if (parameter.startsWith("DAMAGE:")) {
            return parameter.replace("DAMAGE:", "") + " damage";
        } if (parameter.startsWith("CONTROLLER_DAMAGE:")) {
            return "to its controller " + parameter.replace("CONTROLLER_DAMAGE:", "") + " damage";
        } if (parameter.equals(":TAPPED_DOES_NOT_UNTAP_NEXT_TURN")) {
            return "tapped doesn't untap next turn";
        } if (parameter.equals(":RETURN_TO_OWNER_HAND")) {
            return "returned to its owner's hand";
        } if (parameter.startsWith("PLUS_1_COUNTERS:")) {
            return parameter.replace("PLUS_1_COUNTERS:", "") + " +1/+1 counters";
        } else {
            return parameter.replace(":", "").toLowerCase();
        }
    }
}
