package com.matag.game.turn.action.player;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.action._continue.InputRequiredActions.SCRY;

@Component
@AllArgsConstructor
public class ScryXCardsService {
  public void scryXCardsTrigger(Player player, int cardsToScry, GameStatus gameStatus) {
    if (cardsToScry > 0) {
      gameStatus.getTurn().setInputRequiredAction(SCRY);
      gameStatus.getTurn().setInputRequiredActionParameter(String.valueOf(cardsToScry));
    }
  }
}
