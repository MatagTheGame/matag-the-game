package com.matag.game.turn.action.life;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddXLifeAction implements AbilityAction {
  private final LifeService lifeService;
  private final CardInstanceSelectorService cardInstanceSelectorService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    int lifeToAdd = Integer.parseInt(ability.getParameter(0));
    cardInstanceSelectorService.selectPlayers(gameStatus, cardInstance, ability.getCardInstanceSelector())
      .forEach(player -> lifeService.add(player, lifeToAdd, gameStatus));
  }
}
