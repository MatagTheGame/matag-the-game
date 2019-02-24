package com.aa.mtg.game.deck;

import com.aa.mtg.cards.model.Card;
import com.aa.mtg.cards.model.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.security.SecurityToken;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AXEBANE_BEAST;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.FERAL_MAAKA;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, GameStatus gameStatus) {
        Library library = new Library();

        for (int i = 0; i < 60; i++) {
            library.getCards().add(new CardInstance(gameStatus.nextCardId(), getRandomCard()));
        }

        return library;
    }

    private Card getRandomCard() {
        switch (new Random().nextInt(4)) {
            case 0: return MOUNTAIN;
            case 1: return FOREST;
            case 2: return FERAL_MAAKA;
            case 3: return AXEBANE_BEAST;
        }
        throw new RuntimeException("Non Existent Card");
    }

}
