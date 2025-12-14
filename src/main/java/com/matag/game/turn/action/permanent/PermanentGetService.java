package com.matag.game.turn.action.permanent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.AbilityService;
import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.ability.CardInstanceAbilityFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.counters.CountersService;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import com.matag.game.turn.action.leave.PutIntoGraveyardService;
import com.matag.game.turn.action.leave.ReturnPermanentToHandService;
import com.matag.game.turn.action.tap.TapPermanentService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PermanentGetService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PermanentGetService.class);

  private final DealDamageToCreatureService dealDamageToCreatureService;
  private final DealDamageToPlayerService dealDamageToPlayerService;
  private final DestroyPermanentService destroyPermanentService;
  private final TapPermanentService tapPermanentService;
  private final PutIntoGraveyardService putIntoGraveyardService;
  private final ReturnPermanentToHandService returnPermanentToHandService;
  private final GainControlPermanentService gainControlPermanentService;
  private final CountersService countersService;
  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public void thatPermanentGets(CardInstance cardInstance, GameStatus gameStatus, List<String> parameters, CardInstance target) {
    parameters.forEach(parameter -> thatPermanentGets(cardInstance, gameStatus, parameter, target));
  }

  private void thatPermanentGets(CardInstance cardInstance, GameStatus gameStatus, String parameter, CardInstance target) {
    var PowerToughness = abilityService.powerToughnessFromParameter(parameter);
    target.getModifiers().getModifiersUntilEndOfTurn().addExtraPowerToughnessUntilEndOfTurn(PowerToughness);

    var ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);
    ability.ifPresent(value -> target.getModifiers().getModifiersUntilEndOfTurn().getExtraAbilities().add(value));

    var damage = abilityService.damageFromParameter(parameter);
    dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false, cardInstance);

    var controllerDamage = abilityService.controllerDamageFromParameter(parameter);
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

    if (abilityService.cancelledFromParameter(parameter)) {
      var cardToCancel = gameStatus.getStack().search().withId(target.getId());
      cardToCancel.ifPresent((card) -> {
        gameStatus.getStack().remove(card);
        putIntoGraveyardService.putIntoGraveyard(gameStatus, card);
      });
    }

    if (abilityService.returnToOwnerHandFromParameter(parameter)) {
      returnPermanentToHandService.markAsToBeReturnedToHand(gameStatus, target.getId());
    }

    if (abilityService.controlledFromParameter(parameter)) {
      gainControlPermanentService.gainControlUntilEndOfTurn(target, cardInstance.getController());
    }

    var plusCounters = abilityService.plus1CountersFromParameter(parameter);
    countersService.addPlus1Counters(target, plusCounters);

    var minusCounters = abilityService.minus1CountersFromParameter(parameter);
    countersService.addMinus1Counters(target, minusCounters);

    var keywordCounter = abilityService.keywordCounterFromParameter(parameter);
    if (keywordCounter != null) {
      countersService.addKeywordCounter(target, keywordCounter);
    }

    LOGGER.info("PermanentService: {} target {} which gets {}", cardInstance.getIdAndName(), target.getIdAndName(), parameter);
  }
}
