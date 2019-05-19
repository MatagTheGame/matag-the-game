package com.aa.mtg.game.deck;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.aa.mtg.cards.Cards.*;
import static com.aa.mtg.cards.sets.Ixalan.*;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.*;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, String playerName, GameStatus gameStatus) {
        return randomDeck(playerName, gameStatus);
    }

    private Library randomDeck(String playerName, GameStatus gameStatus) {
        switch (new Random().nextInt(4)) {
            case 0:
                return blackWhiteGuild(playerName, gameStatus).shuffle();
            case 1:
                return redGreenGuild(playerName, gameStatus).shuffle();
            case 2:
                return whiteBlueIxalan(playerName, gameStatus).shuffle();
            case 3:
                return redGreenIxalan(playerName, gameStatus).shuffle();
        }
        throw new RuntimeException("Deck not found!");
    }

    private Library blackWhiteGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), PLAINS, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), SWAMP, playerName));
        }


        for (int i = 0; i < 36 / 3; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), CATACOMB_CROCODILE, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), NOXIOUS_GROODION, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), PROWLING_CARACAL, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), CIVIC_STALWART, playerName));
        }

        return new Library(cards);
    }

    private Library redGreenGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), FOREST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), MOUNTAIN, playerName));
        }

        for (int i = 0; i < 36 / 3; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), FERAL_MAAKA, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), AXEBANE_BEAST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), PRECISION_BOLT, playerName));
        }

        return new Library(cards);
    }

    private Library whiteBlueIxalan(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), PLAINS, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), ISLAND, playerName));
        }

        for (int i = 0; i < 36 / 4; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), HEADWATER_SENTRIES, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), HUATLIS_SNUBHORN, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), AIR_ELEMENTAL, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), LEGIONS_JUDGMENT, playerName));
        }

        return new Library(cards);
    }

    private Library redGreenIxalan(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), FOREST, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), MOUNTAIN, playerName));
        }

        for (int i = 0; i < 36 / 6; i++) {
            cards.add(new CardInstance(gameStatus.nextCardId(), NEST_ROBBER, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), ANCIENT_BRONTODON, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), FRENZIED_RAPTOR, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), GRAZING_WHIPTAIL, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), CHARGING_MONSTROSAUR, playerName));
            cards.add(new CardInstance(gameStatus.nextCardId(), COLOSSAL_DREADMAW, playerName));
        }

        return new Library(cards);
    }
}
