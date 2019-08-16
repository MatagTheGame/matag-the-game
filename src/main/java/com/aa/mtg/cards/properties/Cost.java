package com.aa.mtg.cards.properties;

public enum Cost {
    COLORLESS, WHITE, BLUE, BLACK, RED, GREEN;

    public static Cost fromColor(Color color) {
        switch (color) {
            case WHITE:
                return WHITE;
            case BLUE:
                return BLUE;
            case BLACK:
                return BLACK;
            case RED:
                return RED;
            case GREEN:
                return GREEN;
        }
        throw new IllegalArgumentException("Incorrect color: " + color);
    }
}
