package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Service
public class CreaturesYouControlGetXUntilEndOfTurnAction implements AbilityAction {
    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final ThatTargetsGetAction thatTargetsGetAction;

    public CreaturesYouControlGetXUntilEndOfTurnAction(GameStatusUpdaterService gameStatusUpdaterService, ThatTargetsGetAction thatTargetsGetAction) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.thatTargetsGetAction = thatTargetsGetAction;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        Player controller = gameStatus.getPlayerByName(cardInstance.getController());

        List<CardInstance> cards = new CardInstanceSearch(controller.getBattlefield().getCards()).ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            thatTargetsGetAction.thatTargetGet(cardInstance, gameStatus, parameter, card);
        }

        gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, controller);
    }

}
