package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.ability.action.AbilityActionFactory;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetType;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.PlayerType;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.target.TargetStatusType.ATTACKING;
import static com.aa.mtg.cards.ability.target.TargetStatusType.BLOCKING;
import static com.aa.mtg.cards.ability.target.TargetType.ANY;
import static com.aa.mtg.cards.ability.target.TargetType.PERMANENT;
import static com.aa.mtg.cards.ability.type.AbilityType.abilityType;

@Service
public class TargetCheckerService {

    private final AbilityActionFactory abilityActionFactory;

    @Autowired
    public TargetCheckerService(AbilityActionFactory abilityActionFactory) {
        this.abilityActionFactory = abilityActionFactory;
    }

    public void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
        for (Ability ability : cardToCast.getAbilities()) {
            AbilityAction abilityAction = abilityActionFactory.getAbilityAction(abilityType(playedAbility));
            if (abilityAction != null && ability.requiresTarget()) {
                if (targetsIdsForCardIds == null || !targetsIdsForCardIds.containsKey(cardToCast.getId()) || targetsIdsForCardIds.get(cardToCast.getId()).isEmpty()) {
                    throw new MessageException(cardToCast.getIdAndName() + " requires a valid target.");
                }

                List<Object> targetIds = targetsIdsForCardIds.get(cardToCast.getId());
                for (int i = 0; i < ability.getTargets().size(); i++) {
                    check(gameStatus, ability.getTargets().get(i), targetIds.get(i));
                }

                cardToCast.getModifiers().setTargets(targetIds);
            }
        }
    }

    public boolean checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus) {
        for (Ability ability : cardToCast.getAbilities()) {
            AbilityAction abilityAction = abilityActionFactory.getAbilityAction(abilityType("THAT_TARGETS_GET"));
            if (abilityAction == null) {
                return true;

            } else if (!ability.requiresTarget()) {
                return true;

            } else {
                for (Target target : ability.getTargets()) {
                    CardInstanceSearch cards = getPossibleTargetCardInstances(gameStatus, target);
                    if (cards.isNotEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void check(GameStatus gameStatus, Target target, Object targetId) {
        if (targetId instanceof String) {
            if (!(target.getTargetType().equals(TargetType.PLAYER) || target.getTargetType().equals(ANY))) {
                throw new MessageException(targetId + " is not valid for targetType PERMANENT");
            }

        } else {
            CardInstanceSearch cards = getPossibleTargetCardInstances(gameStatus, target);
            int targetCardId = (int) targetId;
            if (!cards.withId(targetCardId).isPresent()) {
                throw new MessageException("Selected targets were not valid.");
            }
        }
    }

    private CardInstanceSearch getPossibleTargetCardInstances(GameStatus gameStatus, Target target) {
        CardInstanceSearch cards;
        if (target.getTargetType().equals(PERMANENT)) {
            cards = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

            if (target.getOfType() != null) {
                cards = cards.ofAnyOfTheTypes(target.getOfType());
            }

            if (target.getTargetPowerToughnessConstraint() != null) {
                cards = cards.ofTargetPowerToughnessConstraint(target.getTargetPowerToughnessConstraint());
            }

            if (target.getTargetStatusTypes() != null) {
                if (target.getTargetStatusTypes().contains(ATTACKING) && target.getTargetStatusTypes().contains(BLOCKING)) {
                    cards = cards.attackingOrBlocking();
                } else if (target.getTargetStatusTypes().contains(ATTACKING)) {
                    cards = cards.attacking();
                } else if (target.getTargetStatusTypes().contains(BLOCKING)) {
                    cards = cards.blocking();
                }
            }

        } else if (target.getTargetType().equals(ANY)) {
            cards = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

        } else {
            throw new RuntimeException("Missing targetType.");
        }

        if (target.getTargetControllerType() == PlayerType.PLAYER) {
            cards = cards.controlledBy(gameStatus.getCurrentPlayer().getName());
        } else if (target.getTargetControllerType() == PlayerType.OPPONENT) {
            cards = cards.controlledBy(gameStatus.getNonCurrentPlayer().getName());
        }

        return cards;
    }
}
