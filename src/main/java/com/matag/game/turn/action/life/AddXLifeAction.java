package com.matag.game.turn.action.life;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstanceSelectorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddXLifeAction implements AbilityAction {
  private final LifeService lifeService;
  private final MagicInstanceSelectorService magicInstanceSelectorService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    int lifeToAdd = Integer.parseInt(ability.getParameter(0));
    magicInstanceSelectorService.selectPlayers(gameStatus, cardInstance, ability.getMagicInstanceSelector())
      .forEach(player -> lifeService.add(player, lifeToAdd, gameStatus));
  }
}
