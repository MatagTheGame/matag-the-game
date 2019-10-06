package com.aa.mtg.cards.ability;

import static com.aa.mtg.cards.ability.trigger.Trigger.activatedAbility;
import static com.aa.mtg.cards.ability.trigger.Trigger.castTrigger;
import static com.aa.mtg.cards.ability.trigger.Trigger.manaAbilityTrigger;
import static com.aa.mtg.cards.ability.trigger.Trigger.staticAbility;
import static com.aa.mtg.cards.ability.trigger.Trigger.triggeredAbility;
import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_IT_ENTERS_THE_BATTLEFIELD;
import static com.aa.mtg.cards.ability.type.AbilityType.ADD_X_LIFE;
import static com.aa.mtg.cards.ability.type.AbilityType.DRAW_X_CARDS;
import static com.aa.mtg.cards.ability.type.AbilityType.EACH_PLAYERS_ADD_X_LIFE;
import static com.aa.mtg.cards.ability.type.AbilityType.ENCHANTED_CREATURE_GETS;
import static com.aa.mtg.cards.ability.type.AbilityType.EQUIPPED_CREATURE_GETS;
import static com.aa.mtg.cards.ability.type.AbilityType.SELECTED_PERMANENTS_GET;
import static com.aa.mtg.cards.ability.type.AbilityType.SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER;
import static com.aa.mtg.cards.ability.type.AbilityType.TAP_ADD_MANA;
import static com.aa.mtg.cards.ability.type.AbilityType.THAT_TARGETS_GET;
import static com.aa.mtg.cards.modifiers.PowerToughness.powerToughness;
import static com.aa.mtg.cards.properties.Cost.COLORLESS;
import static com.aa.mtg.cards.properties.Subtype.KNIGHT;
import static com.aa.mtg.cards.properties.Subtype.ZOMBIE;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.ENCHANTMENT;
import static com.aa.mtg.cards.properties.Type.LAND;
import static com.aa.mtg.cards.properties.Type.PLANESWALKER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.GREATER_OR_EQUAL;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.LESS_OR_EQUAL;
import static com.aa.mtg.cards.selector.SelectorType.PERMANENT;
import static com.aa.mtg.cards.selector.StatusType.ATTACKING;
import static com.aa.mtg.cards.selector.StatusType.BLOCKING;
import static com.aa.mtg.cards.selector.TurnStatusType.YOUR_TURN;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.utils.Utils.replaceLast;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.selector.CardInstanceSelector;
import com.aa.mtg.cards.selector.PowerToughnessConstraint;
import com.aa.mtg.cards.selector.SelectorType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Abilities {
    public static final Ability AS_LONG_AS_IS_YOUR_TURN_IT_HAS_LIFELINK = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build(), singletonList("LIFELINK"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), singletonList("+1/+1"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_GET_PLUS_2_0_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), singletonList("+2/+0"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_GET_PLUS_2_1_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), singletonList("+2/+1"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_GET_PLUS_2_2_AND_TRAMPLE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), asList("+2/+2", "TRAMPLE"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, emptyList(), singletonList("TRAMPLE"), castTrigger());
    public static final Ability CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).withAbilityType(AbilityType.FLYING).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability DEAL_3_DAMAGE_TO_ANY_TARGET = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.ANY).build()).build()), singletonList("DAMAGE:3"), castTrigger());
    public static final Ability DEAL_1_DAMAGE_TO_CREATURE_YOU_CONTROL_THAT_CREATURE_GAINS_TRAMPLE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build()).build()), asList("DAMAGE:1", "TRAMPLE"), castTrigger());
    public static final Ability DEAL_4_DAMAGE_TO_TARGET_ATTACKING_OR_BLOCKING_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).statusTypes(asList(ATTACKING, BLOCKING)).build()).build()), singletonList("DAMAGE:4"), castTrigger());
    public static final Ability DEAL_3_DAMAGE_TO_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("DAMAGE:3"), castTrigger());
    public static final Ability DEAL_4_DAMAGE_TO_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("DAMAGE:4"), castTrigger());
    public static final Ability DEAL_4_DAMAGE_TO_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("DAMAGE:4", "CONTROLLER_DAMAGE:2"), castTrigger());
    public static final Ability DEAL_5_DAMAGE_TO_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("DAMAGE:5"), castTrigger());
    public static final Ability DEAL_5_DAMAGE_TO_TARGET_PLAYER_OR_PLANESWALKER = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).build()).build()), singletonList("DAMAGE:5"), castTrigger());
    public static final Ability DEAL_7_DAMAGE_TO_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("DAMAGE:7"), castTrigger());
    public static final Ability DEATHTOUCH = new Ability(AbilityType.DEATHTOUCH);
    public static final Ability DESTROY_TARGET_ARTIFACT_OR_CREATURE_OR_PLANESWALKER = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(ARTIFACT, CREATURE, PLANESWALKER)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_ARTIFACT_OR_ENCHANTMENT = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(ARTIFACT, ENCHANTMENT)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_ARTIFACT_OR_LAND = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(ARTIFACT, LAND)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_ATTACKING_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).statusTypes(singletonList(ATTACKING)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_ATTACKING_OF_BLOCKING_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).statusTypes(asList(ATTACKING, BLOCKING)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_OR_ENCHANTMENT = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(CREATURE, ENCHANTMENT)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_OR_PLANESWALKER_THAT_S_GREEN_OR_WHITE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(CREATURE, PLANESWALKER)).ofColors(asList(Color.GREEN, Color.WHITE)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER_OR_EQUAL, 4)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_WITH_POWER_LESS_OR_EQUAL_3 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).powerToughnessConstraint(new PowerToughnessConstraint(POWER, LESS_OR_EQUAL, 3)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_WITH_TOUGHNESS_GREATER_OR_EQUAL_4 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, GREATER_OR_EQUAL, 4)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability DESTROY_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList(":DESTROYED", "CONTROLLER_DAMAGE:2"), castTrigger());
    public static final Ability DRAW_1_CARD = new Ability(DRAW_X_CARDS, emptyList(), singletonList("1"), castTrigger());
    public static final Ability DRAW_2_CARDS = new Ability(DRAW_X_CARDS, emptyList(), singletonList("2"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("FLYING"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_1_1_AND_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+1/+1", "FLYING"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_FLYING = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+2", "FLYING"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_HASTE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+2", "HASTE"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_LIFELINK = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+2", "LIFELINK"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+3/+2", "VIGILANCE"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_3_3 = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+3/+3"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_PLUS_7_7_AND_TRAMPLE = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+7/+7", "TRAMPLE"), castTrigger());
    public static final Ability ENCHANTED_CREATURE_GETS_MINUS_2_2 = new Ability(ENCHANTED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("-2/-2"), castTrigger());
    public static final Ability ENTERS_THE_BATTLEFIELD_TAPPED = new Ability(AbilityType.ENTERS_THE_BATTLEFIELD_TAPPED);
    public static final Ability FLYING = new Ability(AbilityType.FLYING);
    public static final Ability GAIN_CONTROL_TARGET_CREATURE_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build()).build()), singletonList(":CONTROLLED"), castTrigger());
    public static final Ability GAIN_1_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("1"), castTrigger());
    public static final Ability GAIN_2_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("2"), castTrigger());
    public static final Ability GAIN_3_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("3"), castTrigger());
    public static final Ability GAIN_4_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("4"), castTrigger());
    public static final Ability HASTE = new Ability(AbilityType.HASTE);
    public static final Ability LIFELINK = new Ability(AbilityType.LIFELINK);
    public static final Ability LOSE_4_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("-4"), castTrigger());
    public static final Ability OTHER_CREATURES_YOU_CONTROL_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability OTHER_CREATURES_YOU_CONTROL_GET_TRAMPLE = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build(), singletonList("TRAMPLE"), staticAbility());
    public static final Ability OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_0_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).withAbilityType(AbilityType.FLYING).others(true).build(), singletonList("+0/+1"), staticAbility());
    public static final Ability OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_1_0 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).withAbilityType(AbilityType.FLYING).others(true).build(), singletonList("+1/+0"), staticAbility());
    public static final Ability OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).withAbilityType(AbilityType.FLYING).others(true).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability OTHER_KNIGHTS_YOU_CONTROL_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtypeOf(singletonList(KNIGHT)).controllerType(PLAYER).others(true).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability OTHER_ZOMBIES_YOU_CONTROL_GET_DEATHTOUCH = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtypeOf(singletonList(ZOMBIE)).controllerType(PLAYER).others(true).build(), singletonList("DEATHTOUCH"), staticAbility());
    public static final Ability OTHER_ZOMBIES_YOU_CONTROL_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtypeOf(singletonList(ZOMBIE)).controllerType(PLAYER).others(true).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability PAY_1_EQUIP_CREATURE_GETS_FLYING = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("FLYING"), activatedAbility(singletonList(COLORLESS)));
    public static final Ability PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1 = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+1/+1"), activatedAbility(singletonList(COLORLESS)));
    public static final Ability PAY_1_EQUIP_CREATURE_GETS_PLUS_1_0_AND_HASTE = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+1/+0", "HASTE"), activatedAbility(singletonList(COLORLESS)));
    public static final Ability PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1_AND_HASTE = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+1/+1", "HASTE"), activatedAbility(singletonList(COLORLESS)));
    public static final Ability PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0 = new Ability(EQUIPPED_CREATURE_GETS, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+2/+0"), activatedAbility(asList(COLORLESS, COLORLESS)));
    public static final Ability PUT_A_PLUS_1_COUNTER_UP_TO_2_TARGET_CREATURES = new Ability(THAT_TARGETS_GET, asList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).optional(true).build(), Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).optional(true).other(true).build()), singletonList("PLUS_1_COUNTERS:1"), castTrigger());
    public static final Ability REACH = new Ability(AbilityType.REACH);
    public static final Ability RETURN_TARGET_NONLAND_TO_ITS_OWNER_HAND = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build()).build()), singletonList(":RETURN_TO_OWNER_HAND"), castTrigger());
    public static final Ability SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER = new Ability(SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).build()).build()), emptyList(), castTrigger());
    public static final Ability SKELETON_YOU_CONTROL_GET_DEATHTOUCH = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtypeOf(singletonList(ZOMBIE)).controllerType(PLAYER).build(), singletonList("DEATHTOUCH"), staticAbility());
    public static final Ability SKELETON_YOU_CONTROL_GET_PLUS_1_1 = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtypeOf(singletonList(ZOMBIE)).controllerType(PLAYER).build(), singletonList("+1/+1"), staticAbility());
    public static final Ability TAP_ADD_BLACK_MANA = new Ability(TAP_ADD_MANA, emptyList(), singletonList("BLACK"), manaAbilityTrigger());
    public static final Ability TAP_ADD_BLUE_MANA = new Ability(TAP_ADD_MANA, emptyList(), singletonList("BLUE"), manaAbilityTrigger());
    public static final Ability TAP_ADD_GREEN_MANA = new Ability(TAP_ADD_MANA, emptyList(), singletonList("GREEN"), manaAbilityTrigger());
    public static final Ability TAP_ADD_1_GREEN_1_BLUE_MANA = new Ability(TAP_ADD_MANA, emptyList(), asList("GREEN", "BLUE"), manaAbilityTrigger());
    public static final Ability TAP_ADD_RED_MANA = new Ability(TAP_ADD_MANA, emptyList(), singletonList("RED"), manaAbilityTrigger());
    public static final Ability TAP_ADD_WHITE_MANA = new Ability(TAP_ADD_MANA, emptyList(), singletonList("WHITE"), manaAbilityTrigger());
    public static final Ability TARGET_BLOCKING_CREATURE_GETS_PLUS_7_7_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).statusTypes(singletonList(BLOCKING)).build()).build()), singletonList("+7/+7"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_DEATHTOUCH_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("DEATHTOUCH"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_HASTE_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("HASTE"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_MINUS_2_2_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("-2/-2"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_1_0_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+1/+0"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_1_1_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+1/+1"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_1_3_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+1/+3"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_1_7_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+1/+7"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_2_1_AND_DEATHTOUCH_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+1", "DEATHTOUCH"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_2_2_AND_FLYING_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+2", "FLYING"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_2_2_AND_LIFELINK_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+2/+2", "LIFELINK"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_3_2_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+3/+2"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_3_3_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+3/+3"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_3_3_AND_REACH_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+3/+3", "REACH"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_PLUS_3_3_AND_TRAMPLE_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), asList("+3/+3", "TRAMPLE"), castTrigger());
    public static final Ability TARGET_CREATURE_GETS_MINUS_4_0_UNTIL_END_OF_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("-4/-0"), castTrigger());
    public static final Ability TARGET_ENCHANTMENT_GETS_DESTROYED = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(ENCHANTMENT)).build()).build()), singletonList(":DESTROYED"), castTrigger());
    public static final Ability TARGET_PLAYER_DRAWS_7_CARDS = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).build()).build()), singletonList("DRAW:7"), castTrigger());
    public static final Ability TRAMPLE = new Ability(AbilityType.TRAMPLE);
    public static final Ability UNTAP_TARGET_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList(":UNTAPPED"), castTrigger());
    public static final Ability VIGILANCE = new Ability(AbilityType.VIGILANCE);
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_ANOTHER_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1_AND_FLYING = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build()).build()), asList("+1/+1", "Flying"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_ANY_TARGET_GET_1_DAMAGE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.ANY).build()).build()), singletonList("DAMAGE:1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), singletonList("+1/+1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_AND_VIGILANCE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), asList("+1/+1", "VIGILANCE"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_2_2_VIGILANCE_AND_TRAMPLE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), asList("+2/+2", "VIGILANCE", "TRAMPLE"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_DESTROY_TARGET_OPPONENT_CREATURE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(OPPONENT).build()).build()), singletonList(":DESTROYED"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD = new Ability(DRAW_X_CARDS, emptyList(), singletonList("1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE = new Ability(EACH_PLAYERS_ADD_X_LIFE, emptyList(), singletonList("4"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_2_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("2"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_3_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("3"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_4_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("4"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_5_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("5"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_LOSE_1_LIFE = new Ability(ADD_X_LIFE, emptyList(), singletonList("-1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_GET_1_DAMAGE = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).others(true).build(), singletonList("DAMAGE:1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build(), singletonList("TRAMPLE"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_YOU_CONTROL_GET_VIGILANCE_UNTIL_END_OF_TURN = new Ability(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build(), singletonList("VIGILANCE"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TAP_TARGET_OPPONENT_CREATURE_DOES_NOT_UNTAP_NEXT_TURN = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(OPPONENT).build()).build()), singletonList(":TAPPED_DOES_NOT_UNTAP_NEXT_TURN"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_0_1 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+0/+1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_2_0 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+2/+0"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_2_2 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).build()).build()), singletonList("+2/+2"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1 = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build()).build()), singletonList("+1/+1"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_OPPONENT_LOSES_3_LIFE = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(OPPONENT).build()).build()), singletonList("LIFE:-3"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_NONLAND_OPPONENT_PERMANENT_GET_DESTROYED = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).controllerType(OPPONENT).build()).build()), singletonList(":DESTROYED"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_RETURN_ANOTHER_TARGET_CREATURE_YOU_CONTROL_TO_ITS_OWNER_HAND = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build()).build()), singletonList(":RETURN_TO_OWNER_HAND"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));
    public static final Ability WHEN_IT_ENTERS_THE_BATTLEFIELD_RETURN_TARGET_OPPONENT_CREATURE_TO_ITS_OWNERS_HAND = new Ability(THAT_TARGETS_GET, singletonList(Target.builder().cardInstanceSelector(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(OPPONENT).build()).build()), singletonList(":RETURN_TO_OWNER_HAND"), triggeredAbility(WHEN_IT_ENTERS_THE_BATTLEFIELD));

    private static Ability get(String ability) {
        try {
            return (Ability) Abilities.class.getField(ability).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ability " + ability  + " does not exist.");
        }
    }

    public static List<Ability> abilitiesFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> !parameter.contains("/") && !parameter.contains(":"))
                .map(Abilities::get)
                .collect(toList());
    }

    public static PowerToughness powerToughnessFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> parameter.contains("/"))
                .map(PowerToughness::powerToughness)
                .findFirst()
                .orElse(powerToughness("0/0"));
    }

    public static Optional<Ability> abilityFromParameter(String parameter) {
        List<Ability> abilities = abilitiesFromParameters(singletonList(parameter));
        if (abilities.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(abilities.get(0));
        }
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
        String text = parameters.stream().map(Abilities::parameterAsString).collect(Collectors.joining(", "));
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
