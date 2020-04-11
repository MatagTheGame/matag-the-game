package com.matag.game.turn.action.life;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EachPlayersAddXLifeAction implements AbilityAction {
  private final LifeService lifeService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    int lifeToAdd = Integer.valueOf(ability.getParameter(0));
    lifeService.add(gameStatus.getPlayer1(), lifeToAdd, gameStatus);
    lifeService.add(gameStatus.getPlayer2(), lifeToAdd, gameStatus);
  }
}
