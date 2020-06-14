package com.matag.game.turn.action.permanent;

import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;
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
    target.getModifiers().getModifiersUntilEndOfTurn().addExtraPowerToughnessUntilEndOfTurn(PowerToughness);

    Optional<CardInstanceAbility> ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);
    ability.ifPresent(value -> target.getModifiers().getModifiersUntilEndOfTurn().getExtraAbilities().add(value));

    int damage = abilityService.damageFromParameter(parameter);
    dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false, cardInstance);

    int controllerDamage = abilityService.controllerDamageFromParameter(parameter);
    dealDamageToPlayerService.dealDamageToPlayer(gameStatus, controllerDamage, gameStatus.getPlayerByName(target.getController()));

    if (abilityService.destroyedFromParameter(parameter)) {
      destroyPermanentService.markToBeDestroyed(gameStatus, target.getId());
    }

    if (abilityService.tappedFromParameter(parameter)) {
      tapPermanentService.tap(gameStatus, target.getId());
    }

    if (abilityService.doesNotUntapNextTurnFromParameter(parameter)) {
      tapPermanentService.doesNotUntapNextTurn(gameStatus, target.getId());
    }

    if (abilityService.untappedFromParameter(parameter)) {
      tapPermanentService.untap(gameStatus, target.getId());
    }

    if (abilityService.returnToOwnerHandFromParameter(parameter)) {
      returnPermanentToHandService.markAsToBeReturnedToHand(gameStatus, target.getId());
    }

    if (abilityService.controlledFromParameter(parameter)) {
      gainControlPermanentService.gainControlUntilEndOfTurn(target, cardInstance.getController());
    }

    int plusCounters = abilityService.plus1CountersFromParameter(parameter);
    countersService.addPlus1Counters(target, plusCounters);

    int minusCounters = abilityService.minus1CountersFromParameter(parameter);
    countersService.addMinus1Counters(target, minusCounters);

    AbilityType keywordCounter = abilityService.keywordCounterFromParameter(parameter);
    if (keywordCounter != null) {
      countersService.addKeywordCounter(target, keywordCounter);
    }

    LOGGER.info("PermanentService: {} target {} which gets {}", cardInstance.getIdAndName(), target.getIdAndName(), parameter);
  }
}
