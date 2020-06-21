package com.matag.game.turn.action.selection;

import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.player.PlayerType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MagicInstancePlayerSelectorService {
  public List<Player> selectPlayers(GameStatus gameStatus, CardInstance cardInstance, MagicInstanceSelector magicInstanceSelector) {
    List<Player> players = new ArrayList<>();

    if (magicInstanceSelector.getSelectorType().equals(SelectorType.PLAYER)) {
      if (magicInstanceSelector.isItself()) {
        players.add(gameStatus.getPlayerByName(cardInstance.getController()));

      } else {
        Player player = gameStatus.getPlayerByName(cardInstance.getController());
        Player opponent = gameStatus.getOtherPlayer(player);
        players.add(opponent);
        if (magicInstanceSelector.getControllerType() != PlayerType.OPPONENT) {
          players.add(player);
        }
      }
    }

    return players;
  }
}
