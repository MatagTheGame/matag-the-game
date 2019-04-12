package com.aa.mtg.game.deck;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.Ixalan.NEST_ROBBER;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.*;

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
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 5; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), SWAMP, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), PLAINS, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), CATACOMB_CROCODILE, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), NOXIOUS_GROODION, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), PROWLING_CARACAL, playerName));
        }

        return new Library(cards);
    }

    private Library plutoDeck(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 5; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), MOUNTAIN, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), FOREST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), FERAL_MAAKA, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), AXEBANE_BEAST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), NEST_ROBBER, playerName));
        }

        return new Library(cards);
    }

}
