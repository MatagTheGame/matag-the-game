package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.turn.action.ability.AbilityActionFactory;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.trigger.TriggerType.CAST;

@Component
public class ResolveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveService.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final ContinueTurnService continueTurnService;
    private final AbilityActionFactory abilityActionFactory;
    private final EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;
    private final TargetCheckerService targetCheckerService;

    @Autowired
    public ResolveService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService, AbilityActionFactory abilityActionFactory,
                          EnterCardIntoBattlefieldService enterCardIntoBattlefieldService, TargetCheckerService targetCheckerService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
        this.abilityActionFactory = abilityActionFactory;
        this.enterCardIntoBattlefieldService = enterCardIntoBattlefieldService;
        this.targetCheckerService = targetCheckerService;
    }

    public void resolve(GameStatus gameStatus, String triggeredNonStackAction, List<Integer> targetCardIds, Map<Integer, List<Object>> targetsIdsForCardIds) {
        if (!gameStatus.getStack().isEmpty()) {
            CardInstance stackItemToResolve = gameStatus.getStack().peek();

            if (stackItemToResolve.getTriggeredAbilities().isEmpty()) {
                gameStatus.getStack().remove();
                resolveCardInstanceFromStack(gameStatus, stackItemToResolve);

            } else {
                Player playerWhoCastedTheSpell = gameStatus.getPlayerByName(stackItemToResolve.getController());
                if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(playerWhoCastedTheSpell.getName())) {
                    if (targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(stackItemToResolve, gameStatus)) {
                        targetCheckerService.checkSpellOrAbilityTargetRequisites(stackItemToResolve, gameStatus, targetsIdsForCardIds, "THAT_TARGETS_GET");
                    } else {
                        gameStatus.getStack().remove();
                        gameStatusUpdaterService.sendUpdateStack(gameStatus);
                        return;
                    }

                } else {
                    gameStatus.getStack().remove();
                    resolveTriggeredAbility(gameStatus, stackItemToResolve);
                }
            }

            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonActivePlayer().getName());
            gameStatusUpdaterService.sendUpdateStack(gameStatus);
            gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else if (gameStatus.getTurn().getTriggeredNonStackAction().equals(triggeredNonStackAction)) {
            resolveTriggeredNonStackAction(gameStatus, triggeredNonStackAction, targetCardIds);

        } else {
            String message = "Cannot resolve triggeredNonStackAction " + triggeredNonStackAction + " as current triggeredNonStackAction is " + gameStatus.getTurn().getTriggeredNonStackAction();
            throw new MessageException(message);
        }
    }

    private void resolveCardInstanceFromStack(GameStatus gameStatus, CardInstance cardToResolve) {
        performAbilitiesActions(gameStatus, cardToResolve, cardToResolve.getAbilitiesByTriggerType(CAST));

        if (cardToResolve.isPermanent()) {
            enterCardIntoBattlefieldService.enter(gameStatus, cardToResolve);

        } else {
            gameStatus.putIntoGraveyard(cardToResolve);
        }

        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
        gameStatusUpdaterService.sendUpdateGraveyards(gameStatus);
    }

    private void resolveTriggeredAbility(GameStatus gameStatus, CardInstance stackItemToResolve) {
        performAbilitiesActions(gameStatus, stackItemToResolve, stackItemToResolve.getTriggeredAbilities());
        stackItemToResolve.getTriggeredAbilities().clear();
    }

    private void resolveTriggeredNonStackAction(GameStatus gameStatus, String triggeredNonStackAction, List<Integer> cardIds) {
        switch (triggeredNonStackAction) {
            case "DISCARD_A_CARD": {
                CardInstance cardInstance = gameStatus.getCurrentPlayer().getHand().extractCardById(cardIds.get(0));
                gameStatus.putIntoGraveyard(cardInstance);
                gameStatusUpdaterService.sendUpdatePlayerHand(gameStatus, gameStatus.getCurrentPlayer());
                gameStatusUpdaterService.sendUpdatePlayerGraveyard(gameStatus, gameStatus.getCurrentPlayer());
                gameStatus.getTurn().setTriggeredNonStackAction(null);
                break;
            }
        }
        continueTurnService.continueTurn(gameStatus);
    }

    private void performAbilitiesActions(GameStatus gameStatus, CardInstance cardToResolve, List<Ability> abilities) {
        for (Ability ability : abilities) {
            performAbilityAction(gameStatus, cardToResolve, ability);
        }

        cardToResolve.getModifiers().setTargets(new ArrayList<>());
    }

    private void performAbilityAction(GameStatus gameStatus, CardInstance cardToResolve, Ability ability) {
        AbilityAction firstAbilityAction = abilityActionFactory.getAbilityAction(ability.getFirstAbilityType());
        if (firstAbilityAction != null) {
            try {
                for (int i = 0; i < ability.getTargets().size(); i++) {
                    targetCheckerService.check(gameStatus, cardToResolve, ability.getTargets().get(i), cardToResolve.getModifiers().getTargets().get(i));
                }

                firstAbilityAction.perform(cardToResolve, gameStatus, ability.getParameter(0));

                for (int i = 1; i < ability.getAbilityTypes().size(); i++) {
                    AbilityAction furtherAbilityAction = abilityActionFactory.getAbilityAction(ability.getAbilityTypes().get(i));
                    furtherAbilityAction.perform(cardToResolve, gameStatus, ability.getParameter(i));
                }

            } catch (MessageException e) {
                LOGGER.info("{}: Target is now invalid during resolution, dropping the action. [{}] ", cardToResolve.getIdAndName(), e.getMessage());
            }
        }
    }
}
