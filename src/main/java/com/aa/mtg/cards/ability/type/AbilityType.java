package com.aa.mtg.cards.ability.type;

import java.util.List;

public enum AbilityType {
    CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN("Creatures you control get %s until end of turn."),
    DEATHTOUCH("Deathtouch."),
    DESTROY_TARGET("Destroy target."),
    DRAW_X_CARDS("Draw %s cards."),
    GAIN_X_LIFE("Gain %s life."),
    EACH_PLAYERS_GAIN_X_LIFE("Each player gains %s life."),
    ENCHANTED_CREATURE_GETS("Enchanted creature gets %s."),
    EQUIPPED_CREATURE_GETS("Equipped creature gets %s."),
    FLYING("Flying."),
    HASTE("Haste."),
    LIFELINK("Lifelink."),
    REACH("Reach."),
    SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER("Shuffle graveyard into library."),
    THAT_TARGET_CONTROLLER_GETS("That target controller get %s."),
    THAT_TARGETS_GETS("That targets get %s."),
    TRAMPLE("Trample."),
    VIGILANCE("Vigilance.");

    private String text;

    AbilityType(String text) {
        this.text = text;
    }

    public String getText(List<String> parameters) {
        return String.format(text, parameters.toArray());
    }

    public static AbilityType abilityType(String text) {
        if (text == null) {
            return null;
        }

        return AbilityType.valueOf(text);
    }
}