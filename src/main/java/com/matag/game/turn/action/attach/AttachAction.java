package com.matag.game.turn.action.attach;

import static java.lang.Integer.parseInt;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AttachAction implements AbilityAction {

  private final AttachService attachService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    var target = cardInstance.getModifiers().getTargets().get(0).toString();
    var attachedToId = parseInt(target);
    attachService.attach(gameStatus, cardInstance, attachedToId);
  }
}
