package com.aa.mtg.game.turn.action.attach;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
public class AttachAction implements AbilityAction {

    private final AttachService attachService;

    @Autowired
    public AttachAction(AttachService attachService) {
        this.attachService = attachService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability) {
        String target = cardInstance.getModifiers().getTargets().get(0).toString();
        int attachedToId = parseInt(target);
        attachService.attach(gameStatus, cardInstance, attachedToId);
    }
}
