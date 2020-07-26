package com.matag.game.turn.action.player;

import com.matag.cards.ability.AbilityService;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PlayerGetService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerGetService.class);

  private final AbilityService abilityService;
  private final LifeService lifeService;
  private final DrawXCardsService drawXCardsService;
  private final DealDamageToPlayerService dealDamageToPlayerService;

  public void thatPlayerGets(CardInstance cardInstance, GameStatus gameStatus, List<String> parameters, Player player) {
    parameters.forEach(parameter -> thatPlayerGets(cardInstance, gameStatus, player, parameter));
  }

  private void thatPlayerGets(CardInstance cardInstance, GameStatus gameStatus, Player player, String parameter) {
    dealDamageToPlayerService.dealDamageToPlayer(gameStatus, abilityService.damageFromParameter(parameter), player);
    lifeService.add(player, abilityService.lifeFromParameter(parameter), gameStatus);
    drawXCardsService.drawXCards(player, abilityService.drawFromParameter(parameter), gameStatus);
    LOGGER.info("AbilityActionExecuted: card {}, parameter {}, player {}", cardInstance.getIdAndName(), parameter, player.getName());
  }
}
