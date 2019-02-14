package com.aa.mtg.player;

import com.aa.mtg.cards.CardInstance;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hand {
    private List<CardInstance> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }
}
