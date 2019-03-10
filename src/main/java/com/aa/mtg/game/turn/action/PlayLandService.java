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
        Player activePlayer = gameStatus.getActivePlayer();

        if (!turn.getCurrentPhase().isMainPhase()) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You can only play lands during main phases.");

        } else if (turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND))) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You already played a land this turn.");

        } else {
            CardInstance cardInstance = activePlayer.getHand().extractCardById(cardId);
            turn.addCardToCardsPlayedWithinTurn(cardInstance);
            activePlayer.getBattlefield().addCard(cardInstance);

            gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
            gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
        }
    }

}
