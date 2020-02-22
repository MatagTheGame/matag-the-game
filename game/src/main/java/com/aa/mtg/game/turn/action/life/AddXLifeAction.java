package com.aa.mtg.game.turn.action.life;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cardinstance.ability.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddXLifeAction implements AbilityAction {
  private final LifeService lifeService;

  @Autowired
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
