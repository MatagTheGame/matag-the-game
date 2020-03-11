package com.matag.game.turn.action.attach;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
public class AttachAction implements AbilityAction {

  private final AttachService attachService;

  public AttachAction(AttachService attachService) {
    this.attachService = attachService;
  }

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    String target = cardInstance.getModifiers().getTargets().get(0).toString();
    int attachedToId = parseInt(target);
    attachService.attach(gameStatus, cardInstance, attachedToId);
  }
}
