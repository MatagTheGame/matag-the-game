package com.aa.mtg.game.turn.action.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.service.LifeService;
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
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        int lifeToAdd = Integer.valueOf(parameter);
        Player controller = gameStatus.getPlayerByName(cardInstance.getController());
        lifeService.add(controller, lifeToAdd, gameStatus);
    }
}
