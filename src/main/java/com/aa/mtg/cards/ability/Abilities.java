package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.cards.ability.target.TargetType;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.modifiers.PowerToughness;

import java.util.List;
import java.util.stream.Collectors;

import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.GREATER_OR_EQUAL;
import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_IT_ENTERS_THE_BATTLEFIELD;
import static com.aa.mtg.cards.ability.trigger.TriggerType.ACTIVATED_ABILITY;
import static com.aa.mtg.cards.ability.trigger.TriggerType.CAST;
import static com.aa.mtg.cards.ability.trigger.TriggerType.TRIGGERED_ABILITY;
import static com.aa.mtg.cards.ability.type.AbilityType.*;
import static com.aa.mtg.cards.modifiers.PowerToughness.powerToughness;
import static com.aa.mtg.cards.properties.Cost.COLORLESS;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Abilities {
    public static Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN = new Ability(CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN, emptyList(), singletonList("+1/+1"), new Trigger(TRIGGERED_ABILITY, WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_AND_VIGILANCE_UNTIL_END_OF_TURN = new Ability(CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN, emptyList(), asList("+1/+1", "VIGILANCE"), new Trigger(TRIGGERED_ABILITY, WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD = new Ability(DRAW_X_CARDS, emptyList(), singletonList("1"), new Trigger(TRIGGERED_ABILITY, WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_5_LIFE = new Ability(GAIN_X_LIFE, emptyList(), singletonList("5"), new Trigger(TRIGGERED_ABILITY, WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static Ability DEAL_1_DAMAGE_TO_CREATURE_YOU_CONTROL_THAT_CREATURE_GAINS_TRAMPLE = new Ability(asList(DEALS_X_DAMAGE_TO_TARGET, THAT_TARGETS_GET_X), singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).targetControllerType(PLAYER).build()), asList("1", "Trample"), new Trigger(CAST));
    public static Ability DEAL_3_DAMAGE_TO_ANY_TARGET = new Ability(DEALS_X_DAMAGE_TO_TARGET, singletonList(Target.builder().targetType(TargetType.ANY).build()), singletonList("3"), new Trigger(CAST));
    public static Ability DEATHTOUCH = new Ability(AbilityType.DEATHTOUCH);
    public static Ability DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4 = new Ability(DESTROY_TARGET, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, GREATER_OR_EQUAL, 4)).build()), emptyList(), new Trigger(CAST));
    public static Ability DRAW_1_CARD = new Ability(DRAW_X_CARDS, emptyList(), singletonList("1"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("FLYING"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_1_1_AND_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+1/+1", "FLYING"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_2_2 = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("+2/+2"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+2/+2", "FLYING"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_HASTE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+2/+2", "HASTE"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_LIFELINK = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+2/+2", "LIFELINK"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+3/+2", "VIGILANCE"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_3_3 = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("+3/+3"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_PLUS_7_7_AND_TRAMPLE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+7/+7", "TRAMPLE"), new Trigger(CAST));
    public static Ability ENCHANTED_CREATURE_GETS_MINUS_2_2 = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("-2/-2"), new Trigger(CAST));
    public static Ability PAY_1_EQUIP_CREATURE_GETS_FLYING = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("FLYING"), new Trigger(ACTIVATED_ABILITY, singletonList(COLORLESS)));
    public static Ability PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1 = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("+1/+1"), new Trigger(ACTIVATED_ABILITY, singletonList(COLORLESS)));
    public static Ability PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1_AND_HASTE = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), asList("+1/+1", "HASTE"), new Trigger(ACTIVATED_ABILITY, singletonList(COLORLESS)));
    public static Ability PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0 = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).build()), singletonList("+2/+0"), new Trigger(ACTIVATED_ABILITY, asList(COLORLESS, COLORLESS)));
    public static Ability FLYING = new Ability(AbilityType.FLYING);
    public static Ability HASTE = new Ability(AbilityType.HASTE);
    public static Ability LIFELINK = new Ability(AbilityType.LIFELINK);
    public static Ability REACH = new Ability(AbilityType.REACH);
    public static Ability SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER = new Ability(SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER, singletonList(Target.builder().targetType(TargetType.PLAYER).build()), emptyList(), new Trigger(CAST));
    public static Ability TRAMPLE = new Ability(AbilityType.TRAMPLE);
    public static Ability VIGILANCE = new Ability(AbilityType.VIGILANCE);


    private static Ability get(String ability) {
        try {
            return (Ability) Abilities.class.getField(ability).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ability " + ability  + " does not exist.");
        }
    }

    public static PowerToughness powerToughnessFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> parameter.contains("/"))
                .map(PowerToughness::powerToughness)
                .findFirst()
                .orElse(powerToughness("0/0"));
    }

    public static List<Ability> abilitiesFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> !parameter.contains("/"))
                .map(Abilities::get)
                .collect(Collectors.toList());
    }
}
