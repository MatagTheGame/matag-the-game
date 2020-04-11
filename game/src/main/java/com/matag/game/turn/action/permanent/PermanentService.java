package com.matag.game.turn.action.permanent;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;
import com.matag.cards.ability.AbilityService;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.counters.CountersService;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import com.matag.game.turn.action.leave.ReturnPermanentToHandService;
import com.matag.game.turn.action.tap.TapPermanentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PermanentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PermanentService.class);

  private final DealDamageToCreatureService dealDamageToCreatureService;
  private final DealDamageToPlayerService dealDamageToPlayerService;
  private final DestroyPermanentService destroyPermanentService;
  private final TapPermanentService tapPermanentService;
  private final ReturnPermanentToHandService returnPermanentToHandService;
  private final GainControlPermanentService gainControlPermanentService;
  private final CountersService countersService;
  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public void thatPermanentGets(CardInstance cardInstance, GameStatus gameStatus, List<String> parameters, CardInstance target) {
    for (String parameter : parameters) {
      thatPermanentGets(cardInstance, gameStatus, parameter, target);
    }
  }

  private void thatPermanentGets(CardInstance cardInstance, GameStatus gameStatus, String parameter, CardInstance target) {
    PowerToughness PowerToughness = abilityService.powerToughnessFromParameter(parameter);
    target.getModifiers().addExtraPowerToughnessUntilEndOfTurn(PowerToughness);

    Optional<CardInstanceAbility> ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);
    ability.ifPresent(value -> target.getModifiers().getAbilitiesUntilEndOfTurn().add(value));

    int damage = abilityService.damageFromParameter(parameter);
    dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false);

    int controllerDamage = abilityService.controllerDamageFromParameter(parameter);
    dealDamageToPlayerService.dealDamageToPlayer(gameStatus, controllerDamage, gameStatus.getPlayerByName(cardInstance.getController()));

    if (abilityService.destroyedFromParameter(parameter)) {
      destroyPermanentService.markToBeDestroyed(gameStatus, target.getId());
    }

    if (abilityService.tappedFromParameter(parameter)) {
      tapPermanentService.tap(gameStatus, target.getId());
    }

    if (abilityService.tappedDoesNotUntapNextTurnFromParameter(parameter)) {
      tapPermanentService.tapDoesNotUntapNextTurn(gameStatus, target.getId());
    }

    if (abilityService.untappedFromParameter(parameter)) {
      tapPermanentService.untap(gameStatus, target.getId());
    }

    if (abilityService.returnToOwnerHandFromParameter(parameter)) {
      returnPermanentToHandService.markAsToBeReturnedToHand(gameStatus, target.getId());
    }

    if (abilityService.controlledFromParameter(parameter)) {
      gainControlPermanentService.gainControlUntilEndOfTurn(gameStatus, target, cardInstance.getController());
    }

    int counters = abilityService.plus1CountersFromParameter(parameter);
    countersService.addPlus1Counters(gameStatus, target, counters);

    LOGGER.info("PermanentService: {} target {} which gets {}", cardInstance.getIdAndName(), target.getIdAndName(), parameter);
  }
}
