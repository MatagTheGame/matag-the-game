package com.matag.game.turn.action.attach;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class AttachAction implements AbilityAction {

  private final AttachService attachService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    String target = cardInstance.getModifiers().getTargets().get(0).toString();
    int attachedToId = parseInt(target);
    attachService.attach(gameStatus, cardInstance, attachedToId);
  }
}
