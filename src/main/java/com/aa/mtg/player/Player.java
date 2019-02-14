package com.aa.mtg.player;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import lombok.Data;

import java.util.Random;

import static com.aa.mtg.cards.Cards.*;

@Data
public class Player {

    private String sessionId;
    private Library library;
    private Hand hand;

    public Player(String sessionId) {
        this.sessionId = sessionId;
        this.library = new Library();
        this.hand = new Hand();

        for (int i = 0; i < 60; i++) {
            this.library.getCards().add(new CardInstance(i, getRandomCard()));
        }

        for (int i = 0; i < 7; i++) {
            this.hand.getCards().add(this.library.draw());
        }
    }

    private Card getRandomCard() {
        switch (new Random().nextInt(4)) {
            case 0: return MOUNTAIN;
            case 1: return FOREST;
            case 2: return ALPHA_TYRRANAX;
            case 3: return FERAL_MAAKA;
        }
        throw new RuntimeException("Non Existent Card");
    }

}
