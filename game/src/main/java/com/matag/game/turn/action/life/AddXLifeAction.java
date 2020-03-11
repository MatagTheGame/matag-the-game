package com.matag.game.turn.action.life;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class AddXLifeAction implements AbilityAction {
  private final LifeService lifeService;

  public AddXLifeAction(LifeService lifeService) {
    this.lifeService = lifeService;
  }

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    int lifeToAdd = Integer.parseInt(ability.getParameter(0));
    Player controller = gameStatus.getPlayerByName(cardInstance.getController());
    lifeService.add(controller, lifeToAdd, gameStatus);
  }
}
