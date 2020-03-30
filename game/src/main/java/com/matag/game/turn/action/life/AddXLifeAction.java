package com.matag.game.turn.action.life;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddXLifeAction implements AbilityAction {
  private final LifeService lifeService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    int lifeToAdd = Integer.parseInt(ability.getParameter(0));
    Player controller = gameStatus.getPlayerByName(cardInstance.getController());
    lifeService.add(controller, lifeToAdd, gameStatus);
  }
}
