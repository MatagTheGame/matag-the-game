package com.aa.mtg.game.init.test;

import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.Cards.*;
import static com.aa.mtg.cards.sets.CoreSet2019.MARAUDERS_AXE;
import static com.aa.mtg.cards.sets.CoreSet2020.OVERCOME;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.CANDLELIGHT_VIGIL;
import static com.aa.mtg.cards.sets.Ixalan.*;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CIVIC_STALWART;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CLEAR_THE_MIND;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.PRECISION_BOLT;
import static com.aa.mtg.cards.sets.RivalsOfIxalan.DEAD_WEIGHT;

@Profile("test")
@Component
public class ProdInitTestService extends InitTestService {

    public ProdInitTestService(CardInstanceFactory cardInstanceFactory) {
        this.cardInstanceFactory = cardInstanceFactory;
    }

    @Override
    public void initGameStatus(GameStatus gameStatus) {
        // Current Player
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);
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
        addCardToCurrentPlayerHand(gameStatus, CANDLELIGHT_VIGIL);
        addCardToCurrentPlayerHand(gameStatus, OVERFLOWING_INSIGHT);

        addCardToCurrentPlayerGraveyard(gameStatus, PLAINS);

        // Non Current Player
        addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        addCardToNonCurrentPlayerLibrary(gameStatus, SWAMP);
        addCardToNonCurrentPlayerLibrary(gameStatus, SWAMP);
        addCardToNonCurrentPlayerLibrary(gameStatus, SWAMP);

        addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
        addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);

        addCardToNonCurrentPlayerHand(gameStatus, GILDED_SENTINEL);
        addCardToNonCurrentPlayerHand(gameStatus, CHARGING_MONSTROSAUR);
        addCardToNonCurrentPlayerHand(gameStatus, PRECISION_BOLT);
        addCardToNonCurrentPlayerHand(gameStatus, OVERCOME);
        addCardToNonCurrentPlayerHand(gameStatus, MARAUDERS_AXE);
        addCardToNonCurrentPlayerHand(gameStatus, DEAD_WEIGHT);

        addCardToNonCurrentPlayerGraveyard(gameStatus, MOUNTAIN);
    }
}
