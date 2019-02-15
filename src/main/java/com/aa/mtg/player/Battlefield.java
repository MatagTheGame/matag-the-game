package com.aa.mtg.player;

import com.aa.mtg.cards.CardInstance;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Battlefield {
    private List<CardInstance> cards;

    public Battlefield() {
        this.cards = new ArrayList<>();
    }
}
