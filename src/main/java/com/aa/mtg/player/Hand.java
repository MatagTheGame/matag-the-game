package com.aa.mtg.player;

import com.aa.mtg.cards.CardIntance;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hand {
    private List<CardIntance> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }
}
