package com.aa.mtg.game.init.test;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.Ixalan.*;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CIVIC_STALWART;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CLEAR_THE_MIND;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.PRECISION_BOLT;

@Profile("test")
@Service
public class ProdInitTestService extends InitTestService {

    @Override
    public void initGameStatus(GameStatus gameStatus) {
        // Current Player
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

        addCardToCurrentPlayerHand(gameStatus, HUATLIS_SNUBHORN);
        addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
        addCardToCurrentPlayerHand(gameStatus, AIR_ELEMENTAL);
        addCardToCurrentPlayerHand(gameStatus, CLEAR_THE_MIND);
        addCardToCurrentPlayerHand(gameStatus, CIVIC_STALWART);

        addCardToCurrentPlayerGraveyard(gameStatus, PLAINS);

        // Non Current Player
        addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);

        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);

        addCardToNonCurrentPlayerHand(gameStatus, GRAZING_WHIPTAIL);
        addCardToNonCurrentPlayerHand(gameStatus, CHARGING_MONSTROSAUR);
        addCardToNonCurrentPlayerHand(gameStatus, PRECISION_BOLT);
        addCardToNonCurrentPlayerHand(gameStatus, RILE);

        addCardToNonCurrentPlayerGraveyard(gameStatus, MOUNTAIN);
    }
}
