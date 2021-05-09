package com.matag.game.cardinstance.cost;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.tap.TapPermanentService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PayCostService {
  private final CostService costService;
  private final TapPermanentService tapPermanentService;

  public void pay(GameStatus gameStatus, Player player, CardInstance cardToCast, String playedAbility, Map<Integer, List<String>> paidMana) {
    // FIXME Antonio: Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
    paidMana.keySet().stream()
      .map(cardInstanceId -> player.getBattlefield().findCardById(cardInstanceId))
      .forEach(card -> tapPermanentService.tap(gameStatus, card.getId()));
    gameStatus.getTurn().setLastManaPaid(paidMana);

    if (costService.needsTapping(cardToCast.getCard(), playedAbility)) {
      tapPermanentService.tap(gameStatus, cardToCast.getId());
    }
  }
}
