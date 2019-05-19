package com.aa.mtg.game.stack;

import com.aa.mtg.cards.CardInstance;

import java.util.LinkedList;

public class SpellStack {
    private LinkedList<CardInstance> items = new LinkedList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(CardInstance cardInstance) {
        items.addLast(cardInstance);
    }

    public CardInstance peek() {
        return items.peekLast();
    }

    public CardInstance remove() {
        return items.removeLast();
    }

    public LinkedList<CardInstance> getItems() {
        return items;
    }

    public void setItems(LinkedList<CardInstance> items) {
        this.items = items;
    }
}
