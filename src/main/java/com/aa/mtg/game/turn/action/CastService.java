package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CastService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public CastService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();

        CardInstance cardInstance = activePlayer.getHand().findCardById(cardId);
        if (!turn.getCurrentPhase().isMainPhase() && !cardInstance.getCard().isInstantSpeed()) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You can only play Instants during a NON main phases.");

        } else {
            CardInstance cardToCast = activePlayer.getHand().findCardById(cardId);

            ArrayList<Color> paidCost = new ArrayList<>();
            for (int tappingLandId : tappingLandIds) {
                CardInstance landToTap = activePlayer.getBattlefield().findCardById(tappingLandId);
                if (!landToTap.getCard().getTypes().contains(Type.LAND)) {
                    gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "The card you are trying to tap for mana is not a land.");
                } else if (landToTap.getModifiers().isTapped()) {
                    gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "The land you are trying to tap is already tapped.");
                }
                paidCost.add(landToTap.getCard().getColors().get(0));
            }

            if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost)) {
                gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "There was an error while paying the cost for " + cardToCast.getCard().getName() + ".");

            } else {
                cardInstance = activePlayer.getHand().extractCardById(cardId);
                activePlayer.getBattlefield().addCard(cardInstance);

                // TODO do not tap all lands but only the one necessary to pay the cost above... this gets complicated
                tappingLandIds.stream()
                        .map(tappingLandId -> activePlayer.getBattlefield().findCardById(tappingLandId))
                        .forEach(card -> card.getModifiers().tap());

                gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
                gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
            }
        }
    }
}
