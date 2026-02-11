package com.matag.game.turn.action

import com.matag.cards.ability.AbilityAction
import com.matag.cards.ability.type.AbilityType
import com.matag.game.turn.action.attach.AttachAction
import com.matag.game.turn.action.selection.SelectedPermanentsGetAction
import com.matag.game.turn.action.shuffle.ShuffleTargetGraveyardIntoLibraryAction
import com.matag.game.turn.action.target.ThatTargetsGetAction
import org.springframework.stereotype.Component

@Component
class AbilityActionFactory(
    private val thatTargetsGetAction: ThatTargetsGetAction,
    private val shuffleTargetGraveyardIntoLibraryAction: ShuffleTargetGraveyardIntoLibraryAction,
    private val selectedPermanentsGetAction: SelectedPermanentsGetAction,
    private val attachAction: AttachAction
) {

    fun getAbilityAction(abilityType: AbilityType): AbilityAction? {
        return when (abilityType) {
            AbilityType.SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER -> shuffleTargetGraveyardIntoLibraryAction
            AbilityType.THAT_TARGETS_GET -> thatTargetsGetAction
            AbilityType.SELECTED_PERMANENTS_GET -> selectedPermanentsGetAction
            AbilityType.ENCHANT, AbilityType.EQUIP -> attachAction
            else -> null
        }
    }
}
