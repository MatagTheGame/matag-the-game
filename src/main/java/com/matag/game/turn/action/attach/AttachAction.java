package com.matag.game.turn.action.attach;

import static java.lang.Integer.parseInt;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.ability.AbilityAction;
import com.matag.cards.ability.Ability;
import com.matag.game.status.GameStatus;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AttachAction implements AbilityAction {

  private final AttachService attachService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability) {
    var target = cardInstance.getModifiers().getTargets().get(0).toString();
    var attachedToId = parseInt(target);
    attachService.attach(gameStatus, cardInstance, attachedToId);
  }
}
