package com.aa.mtg.game.deck;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AXEBANE_BEAST;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CATACOMB_CROCODILE;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.FERAL_MAAKA;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.PROWLING_CARACAL;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, String playerName, GameStatus gameStatus) {
        if (playerName.equals("Pippo")) {
            return pippoDeck(playerName, gameStatus);
        } else {
            return plutoDeck(playerName, gameStatus);
        }
    }

    private Library pippoDeck(String playerName, GameStatus gameStatus) {
        return createDeck(playerName, gameStatus, SWAMP, PLAINS, CATACOMB_CROCODILE, PROWLING_CARACAL);
    }

    private Library plutoDeck(String playerName, GameStatus gameStatus) {
        return createDeck(playerName, gameStatus, MOUNTAIN, FOREST, FERAL_MAAKA, AXEBANE_BEAST);
    }

    private Library createDeck(String playerName, GameStatus gameStatus, Card land1, Card land2, Card creature1, Card creature2) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), land1, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), land2, playerName));
        }

        for (int i = 0; i < 18; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), creature1, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), creature2, playerName));
        }

        Collections.shuffle(cards);
        return new Library(cards);
    }

}
