package com.aa.mtg.cards.ability.type;

public enum AbilityType {
    CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN("Creatures you control get %s until end of turn."),
    DEATHTOUCH("Deathtouch."),
    DRAW_X_CARDS("Draw %s cards."),
    ADD_X_LIFE("%s %s life."),
    EACH_PLAYERS_ADD_X_LIFE("Each player %s %s life."),
    ENCHANTED_CREATURE_GETS("Enchanted creature gets %s."),
    ENTERS_THE_BATTLEFIELD_TAPPED("Enters the battlefield tapped."),
    EQUIPPED_CREATURE_GETS("Equipped creature gets %s."),
    FLYING("Flying."),
    HASTE("Haste."),
    LIFELINK("Lifelink."),
    OTHER_CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN("Other creatures you control get %s until end of turn."),
    OTHER_CREATURES_GET_X_UNTIL_END_OF_TURN("Other creatures get %s until end of turn."),
    REACH("Reach."),
    SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER("Shuffle graveyard into library."),
    THAT_TARGET_CONTROLLER_GETS("That target controller gets %s."),
    THAT_TARGETS_GET("That targets get %s."),
    TRAMPLE("Trample."),
    VIGILANCE("Vigilance.");

    private String text;

    AbilityType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static AbilityType abilityType(String text) {
        if (text == null) {
            return null;
        }

        return AbilityType.valueOf(text);
    }
}