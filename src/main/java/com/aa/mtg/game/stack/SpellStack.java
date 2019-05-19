package com.aa.mtg.game.stack;

import com.aa.mtg.cards.CardInstance;

import java.util.LinkedList;

public class SpellStack {

    private LinkedList<Object> items = new LinkedList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(CardInstance cardInstance) {
        items.addLast(cardInstance);
    }

    public Object remove() {
        return items.removeLast();
    }

    public LinkedList<Object> getItems() {
        return items;
    }

    public void setItems(LinkedList<Object> items) {
        this.items = items;
    }
}
