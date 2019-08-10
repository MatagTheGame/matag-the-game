package com.aa.mtg.cards.ability.type;

import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.parametersAsString;

public enum AbilityType {
    CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN("Creatures you control get %s until end of turn."),
    DEATHTOUCH("Deathtouch."),
    DRAW_X_CARDS("Draw %s cards."),
    GAIN_X_LIFE("Gain %s life."),
    EACH_PLAYERS_GAIN_X_LIFE("Each player gains %s life."),
    ENCHANTED_CREATURE_GETS("Enchanted creature gets %s."),
    ENTERS_THE_BATTLEFIELD_TAPPED("Enters the battlefield tapped."),
    EQUIPPED_CREATURE_GETS("Equipped creature gets %s."),
    FLYING("Flying."),
    HASTE("Haste."),
    LIFELINK("Lifelink."),
    REACH("Reach."),
    SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER("Shuffle graveyard into library."),
    THAT_TARGET_CONTROLLER_GETS("That target controller get %s."),
    THAT_TARGETS_GET("That targets get %s."),
    TRAMPLE("Trample."),
    VIGILANCE("Vigilance.");

    private String text;

    AbilityType(String text) {
        this.text = text;
    }

    public String getText(List<String> parameters) {
        return String.format(text, parametersAsString(parameters));
    }

    public static AbilityType abilityType(String text) {
        if (text == null) {
            return null;
        }

        return AbilityType.valueOf(text);
    }
}