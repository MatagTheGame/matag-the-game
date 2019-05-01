package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.ability.action.AbilityActionFactory;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.HASTE;
import static com.aa.mtg.cards.properties.Type.CREATURE;

@Service
public class ResolveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveService.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final ContinueTurnService continueTurnService;
    private final AbilityActionFactory abilityActionFactory;

    @Autowired
    public ResolveService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService, AbilityActionFactory abilityActionFactory) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
        this.abilityActionFactory = abilityActionFactory;
    }

    public void resolve(GameStatus gameStatus, String triggeredAction, List<Integer> cardIds) {
        if (!gameStatus.getStack().isEmpty()) {
            CardInstance cardToResolve = gameStatus.getStack().removeLast();
            gameStatusUpdaterService.sendUpdateStack(gameStatus);

            performAbilityAction(gameStatus, cardToResolve);

            if (cardToResolve.isPermanent()) {
                if (cardToResolve.isOfType(CREATURE) && !cardToResolve.hasAbility(HASTE)) {
                    cardToResolve.getModifiers().setSummoningSickness(true);
                }

                gameStatus.getCurrentPlayer().getBattlefield().addCard(cardToResolve);

            } else {
                gameStatus.putIntoGraveyard(cardToResolve);
            }

            gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
            gameStatusUpdaterService.sendUpdateGraveyards(gameStatus);

            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else if (gameStatus.getTurn().getTriggeredAction().equals(triggeredAction)) {
            switch (triggeredAction) {
                case "DISCARD_A_CARD": {
                    CardInstance cardInstance = gameStatus.getCurrentPlayer().getHand().extractCardById(cardIds.get(0));
                    gameStatus.putIntoGraveyard(cardInstance);
                    gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
                    gameStatusUpdaterService.sendUpdateCurrentPlayerGraveyard(gameStatus);
                    gameStatus.getTurn().setTriggeredAction(null);
                    break;
                }
            }
            continueTurnService.continueTurn(gameStatus);

        } else {
            String message = "Cannot resolve triggeredAction " + triggeredAction + " as current triggeredAction is " + gameStatus.getTurn().getTriggeredAction();
            throw new MessageException(message);
        }
    }

    private void performAbilityAction(GameStatus gameStatus, CardInstance cardToResolve) {
        for (Ability ability : cardToResolve.getAbilities()) {
            AbilityAction abilityAction = abilityActionFactory.getAbilityAction(ability.getAbilityType());
            if (abilityAction != null) {
                try {
                    for (int i = 0; i < ability.getTargets().size(); i++) {
                        ability.getTargets().get(i).check(gameStatus, cardToResolve.getModifiers().getTargets().get(i));
                    }

                    abilityAction.perform(ability, cardToResolve, gameStatus);
                } catch (MessageException e) {
                    LOGGER.info("{}: Target is now invalid during resolution, dropping the action. [{}] ", cardToResolve.getIdAndName(), e.getMessage());
                }
            }
        }
    }
}
