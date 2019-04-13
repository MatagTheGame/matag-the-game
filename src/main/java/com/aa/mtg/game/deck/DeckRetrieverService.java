package com.aa.mtg.game.deck;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.Cards.*;
import static com.aa.mtg.cards.sets.Ixalan.ANCIENT_BRONTODON;
import static com.aa.mtg.cards.sets.Ixalan.HEADWATER_SENTRIES;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.cards.sets.Ixalan.NEST_ROBBER;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.*;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, String playerName, GameStatus gameStatus) {
        if (playerName.equals("Pippo")) {
            return whiteBlueIxalan(playerName, gameStatus);
        } else {
            return redGreenIxalan(playerName, gameStatus);
        }
    }

    private Library blackWhiteGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 5; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), PLAINS, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), SWAMP, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), CATACOMB_CROCODILE, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), NOXIOUS_GROODION, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), PROWLING_CARACAL, playerName));
        }

        return new Library(cards);
    }

    private Library redGreenGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 5; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), FOREST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), MOUNTAIN, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), FERAL_MAAKA, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), AXEBANE_BEAST, playerName));

        }

        return new Library(cards);
    }

    private Library whiteBlueIxalan(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 4; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), PLAINS, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), ISLAND, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), HEADWATER_SENTRIES, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), HUATLIS_SNUBHORN, playerName));
        }

        return new Library(cards);
    }

    private Library redGreenIxalan(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 60 / 4; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), FOREST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), MOUNTAIN, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), NEST_ROBBER, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), ANCIENT_BRONTODON, playerName));
        }

        return new Library(cards);
    }



}
