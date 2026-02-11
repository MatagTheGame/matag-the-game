package com.matag.game.cardinstance.cost

import com.matag.cards.Card
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Cost
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class CostService {
    fun isCastingCostFulfilled(cardInstance: CardInstance, ability: String?, manaPaid: List<Cost>): Boolean {
        val manaPaidCopy = ArrayList<Cost?>(manaPaid)

        if (needsTapping(cardInstance.card!!, ability)) {
            if (cardInstance.modifiers.isTapped || cardInstance.modifiers.isSummoningSickness) {
                return false
            }
        }

        for (cost in getSimpleCost(cardInstance.card!!, ability)) {
            var removed = false

            if (cost == Cost.ANY) {
                if (!manaPaidCopy.isEmpty()) {
                    manaPaidCopy.removeFirst()
                    removed = true
                }
            } else {
                removed = manaPaidCopy.remove(cost)
            }


            if (!removed) {
                return false
            }
        }

        return true
    }

    fun canAfford(cardInstance: CardInstance, ability: String?, gameStatus: GameStatus): Boolean {
        val cardsThatCanGenerateMana = gameStatus.activePlayer.battlefield.search()
            .untapped()
            .withFixedAbility(AbilityType.TAP_ADD_MANA)
            .cards

        val manaOptions = generatePossibleManaOptions(cardsThatCanGenerateMana)

        // try all options
        for (manaOption in manaOptions) {
            if (isCastingCostFulfilled(cardInstance, ability, manaOption)) {
                return true
            }
        }

        return false
    }

    fun generatePossibleManaOptions(cardsThatCanGenerateMana: List<CardInstance>): List<List<Cost>> {
        // calculate number of choices
        val choices: Int = cardsThatCanGenerateMana.stream()
            .map { it.getAbilitiesByType(AbilityType.TAP_ADD_MANA) }
            .map { it.size }
            .reduce { left: Int, right: Int -> left * right }
            .orElse(1)!!

        // populate empty costs
        val manaOptions = ArrayList<MutableList<Cost>>(choices)
        for (j in 0..<choices) {
            manaOptions.add(ArrayList())
        }

        // populate choices
        var inverseCumulativeSizes = choices
        for (instance in cardsThatCanGenerateMana) {
            val addManaAbilities = instance.getAbilitiesByType(AbilityType.TAP_ADD_MANA)
            inverseCumulativeSizes /= addManaAbilities.size

            for (j in 0..<choices) {
                val index = (j / inverseCumulativeSizes) % addManaAbilities.size
                val mana = addManaAbilities.get(index).parameters.stream()
                    .map<Cost> { value: String? -> Cost.valueOf(value!!) }
                    .collect(Collectors.toList())
                manaOptions.get(j).addAll(mana)
            }
        }

        return manaOptions
    }

    fun needsTapping(card: Card, ability: String?): Boolean {
        return getCost(card, ability)!!.stream().anyMatch { c: Cost? -> c == Cost.TAP }
    }

    private fun getSimpleCost(card: Card, ability: String?): MutableList<Cost?> {
        return getCost(card, ability)!!.stream().filter { c: Cost? -> c != Cost.TAP }.collect(Collectors.toList())
    }

    private fun getCost(card: Card, ability: String?): List<Cost>? {
        if (ability == null) {
            return card.cost
        } else {
            return getAbilityCost(card, ability)
        }
    }

    private fun getAbilityCost(card: Card, ability: String): List<Cost>? {
        return card.abilities
            .single { it.abilityType == AbilityType.valueOf(ability) }
            .trigger!!
            .cost
    }
}
