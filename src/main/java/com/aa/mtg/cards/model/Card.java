package com.aa.mtg.cards.model;

public abstract class Card implements Cloneable {
    private final String name;

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
