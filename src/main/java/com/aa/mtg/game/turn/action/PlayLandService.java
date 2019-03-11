package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayLandService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public PlayLandService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void playLand(GameStatus gameStatus, int cardId) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();

        if (!turn.getCurrentPhase().isMainPhase()) {
            gameStatusUpdaterService.sendMessageToCurrentPlayer(currentPlayer, "You can only play lands during main phases.");

        } else if (turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND))) {
            gameStatusUpdaterService.sendMessageToCurrentPlayer(currentPlayer, "You already played a land this turn.");

        } else {
            CardInstance cardInstance = currentPlayer.getHand().findCardById(cardId);
            if (cardInstance.isOfType(Type.LAND)) {
                cardInstance = currentPlayer.getHand().extractCardById(cardId);
                turn.addCardToCardsPlayedWithinTurn(cardInstance);
                currentPlayer.getBattlefield().addCard(cardInstance);

                gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
                gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
            } else {
                gameStatusUpdaterService.sendMessageToCurrentPlayer(currentPlayer, "Playing " + cardInstance.getIdAndName() + " as land.");
            }
        }
    }

}
