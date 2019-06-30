package com.aa.mtg.game.deck;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.aa.mtg.cards.Cards.*;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.CANDLELIGHT_VIGIL;
import static com.aa.mtg.cards.sets.Ixalan.*;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.*;
import static com.aa.mtg.cards.sets.WarOfTheSparks.*;

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
        }
        throw new RuntimeException("Deck not found!");
    }

    private Library blackWhiteGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        addNCards(gameStatus, cards, playerName, 12, PLAINS);
        addNCards(gameStatus, cards, playerName, 12, SWAMP);

        addNCards(gameStatus, cards, playerName, 4, PROWLING_CARACAL);
        addNCards(gameStatus, cards, playerName, 4, CIVIC_STALWART);
        addNCards(gameStatus, cards, playerName, 4, LAZOTEP_BEHEMOTH);
        addNCards(gameStatus, cards, playerName, 4, ENFORCER_GRIFFIN);
        addNCards(gameStatus, cards, playerName, 4, IRONCLAD_KROVOD);
        addNCards(gameStatus, cards, playerName, 4, CATACOMB_CROCODILE);
        addNCards(gameStatus, cards, playerName, 4, NOXIOUS_GROODION);
        addNCards(gameStatus, cards, playerName, 4, CANDLELIGHT_VIGIL);

        return new Library(cards);
    }

    private Library redGreenGuild(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        addNCards(gameStatus, cards, playerName, 12, FOREST);
        addNCards(gameStatus, cards, playerName, 12, MOUNTAIN);

        addNCards(gameStatus, cards, playerName, 4, FERAL_MAAKA);
        addNCards(gameStatus, cards, playerName, 4, AXEBANE_BEAST);
        addNCards(gameStatus, cards, playerName, 4, PRECISION_BOLT);
        addNCards(gameStatus, cards, playerName, 4, NEST_ROBBER);
        addNCards(gameStatus, cards, playerName, 4, ANCIENT_BRONTODON);
        addNCards(gameStatus, cards, playerName, 4, FRENZIED_RAPTOR);
        addNCards(gameStatus, cards, playerName, 4, GRAZING_WHIPTAIL);
        addNCards(gameStatus, cards, playerName, 4, COLOSSAL_DREADMAW);
        addNCards(gameStatus, cards, playerName, 4, GOBLIN_ASSAILANT);
        addNCards(gameStatus, cards, playerName, 4, KRAUL_STINGER);
        addNCards(gameStatus, cards, playerName, 4, PRIMORDIAL_WURM);

        return new Library(cards);
    }

    private Library whiteBlueIxalan(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        addNCards(gameStatus, cards, playerName, 12, PLAINS);
        addNCards(gameStatus, cards, playerName, 12, ISLAND);

        addNCards(gameStatus, cards, playerName, 4, HEADWATER_SENTRIES);
        addNCards(gameStatus, cards, playerName, 4, HUATLIS_SNUBHORN);
        addNCards(gameStatus, cards, playerName, 4, AIR_ELEMENTAL);
        addNCards(gameStatus, cards, playerName, 4, LEGIONS_JUDGMENT);
        addNCards(gameStatus, cards, playerName, 4, CANDLELIGHT_VIGIL);
        addNCards(gameStatus, cards, playerName, 4, NAGA_ETERNAL);

        return new Library(cards);
    }

    private void addNCards(GameStatus gameStatus, List<CardInstance> cards, String playerName, int n, Card plains) {
        for (int i = 0; i < n; i++) {
            cards.add(new CardInstance(gameStatus, gameStatus.nextCardId(), plains, playerName));
        }
    }
}
