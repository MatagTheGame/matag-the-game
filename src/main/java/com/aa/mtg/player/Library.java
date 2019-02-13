package com.aa.mtg.player;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardIntance;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Library {
    private List<CardIntance> cards;

    public Library() {
        this.cards = new ArrayList<>();
    }

    public CardIntance draw() {
        return this.cards.remove(0);
    }
}
