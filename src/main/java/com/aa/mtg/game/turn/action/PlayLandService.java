package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.PhaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayLandService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;

    @Autowired
    public PlayLandService(GameStatusUpdaterService gameStatusUpdaterService, EnterCardIntoBattlefieldService enterCardIntoBattlefieldService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.enterCardIntoBattlefieldService = enterCardIntoBattlefieldService;
    }

    public void playLand(GameStatus gameStatus, int cardId) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();

        if (!PhaseUtils.isMainPhase(turn.getCurrentPhase())) {
            throw new MessageException("You can only play lands during main phases.");

        } else if (turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND))) {
            throw new MessageException("You already played a land this turn.");

        } else {
            CardInstance cardInstance = activePlayer.getHand().findCardById(cardId);
            if (cardInstance.isOfType(Type.LAND)) {
                cardInstance = activePlayer.getHand().extractCardById(cardId);
                cardInstance.setController(activePlayer.getName());
                turn.addCardToCardsPlayedWithinTurn(cardInstance);
                enterCardIntoBattlefieldService.enter(gameStatus, cardInstance);

                gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, activePlayer);
                gameStatusUpdaterService.sendUpdatePlayerHand(gameStatus, activePlayer);
            } else {
                throw new MessageException("Playing " + cardInstance.getIdAndName() + " as land.");
            }
        }
    }

}
