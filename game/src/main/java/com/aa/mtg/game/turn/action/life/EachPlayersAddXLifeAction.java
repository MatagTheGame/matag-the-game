package com.aa.mtg.game.turn.action.life;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EachPlayersAddXLifeAction implements AbilityAction {
    private final LifeService lifeService;

    @Autowired
    public EachPlayersAddXLifeAction(LifeService lifeService) {
        this.lifeService = lifeService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability) {
        int lifeToAdd = Integer.valueOf(ability.getParameter(0));
        lifeService.add(gameStatus.getPlayer1(), lifeToAdd, gameStatus);
        lifeService.add(gameStatus.getPlayer2(), lifeToAdd, gameStatus);
    }
}
