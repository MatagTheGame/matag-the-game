package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.cards.ability.target.TargetSelectionConstraint;
import com.aa.mtg.cards.ability.target.TargetType;

import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.ability.type.AbilityType.DEALS_X_DAMAGE_TO_TARGET;
import static com.aa.mtg.cards.ability.type.AbilityType.DESTROY_TARGET;
import static com.aa.mtg.cards.ability.type.AbilityType.DRAW_X_CARDS;
import static com.aa.mtg.cards.ability.type.AbilityType.SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Abilities {
    public static Ability DEAL_1_DAMAGE_TO_CREATURE_YOU_CONTROL_THAT_CREATURE_GAINS_TRAMPLE = new Ability(DEALS_X_DAMAGE_TO_TARGET, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).targetControllerType(PLAYER).build()), singletonList("1"));
    public static Ability DEAL_3_DAMAGE_TO_ANY_TARGET = new Ability(DEALS_X_DAMAGE_TO_TARGET, singletonList(Target.builder().targetType(TargetType.ANY).build()), singletonList("3"));
    public static Ability DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4 = new Ability(DESTROY_TARGET, singletonList(Target.builder().targetType(TargetType.PERMANENT).ofType(singletonList(CREATURE)).targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, TargetSelectionConstraint.GREATER_OR_EQUAL, 4)).build()), emptyList());
    public static Ability DRAW_1_CARD = new Ability(DRAW_X_CARDS, emptyList(), singletonList("1"));
    public static Ability SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER = new Ability(SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER, singletonList(Target.builder().targetType(TargetType.PLAYER).build()), emptyList());
}
