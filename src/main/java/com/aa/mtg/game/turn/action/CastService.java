package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
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
        Player currentPlayer = gameStatus.getCurrentPlayer();

        CardInstance cardInstance = currentPlayer.getHand().findCardById(cardId);
        if (!turn.getCurrentPhase().isMainPhase() && !cardInstance.getCard().isInstantSpeed()) {
            throw new MessageException("You can only play Instants during a NON main phases.");

        } else {
            CardInstance cardToCast = currentPlayer.getHand().findCardById(cardId);

            ArrayList<Color> paidCost = new ArrayList<>();
            for (int tappingLandId : tappingLandIds) {
                CardInstance landToTap = currentPlayer.getBattlefield().findCardById(tappingLandId);
                if (!landToTap.isOfType(Type.LAND)) {
                    throw new MessageException("The card you are trying to tap for mana is not a land.");
                } else if (landToTap.getModifiers().isTapped()) {
                    throw new MessageException("The land you are trying to tap is already tapped.");
                }
                paidCost.add(landToTap.getCard().getColors().get(0));
            }

            if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost)) {
                throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");

            } else {
                cardInstance = currentPlayer.getHand().extractCardById(cardId);
                cardInstance.setController(currentPlayer.getName());
                gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);

                gameStatus.getStack().addLast(cardInstance);
                gameStatusUpdaterService.sendUpdateStack(gameStatus);

                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);

                // FIXME Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
                tappingLandIds.stream()
                        .map(tappingLandId -> currentPlayer.getBattlefield().findCardById(tappingLandId))
                        .forEach(card -> card.getModifiers().tap());
                gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
            }
        }
    }
}
