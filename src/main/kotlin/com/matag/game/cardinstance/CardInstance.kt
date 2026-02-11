package com.matag.game.cardinstance

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.matag.cards.Card
import com.matag.cards.ability.Ability
import com.matag.cards.ability.Ability.Companion.abilities
import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.ability.trigger.TriggerType
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Subtype
import com.matag.cards.properties.Type
import com.matag.game.cardinstance.modifiers.CardModifiers
import com.matag.game.message.MessageException
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.attach.AttachmentsService
import com.matag.game.turn.action.selection.AbilitiesFromOtherPermanentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Component
@Scope("prototype")
class CardInstance(
    @param:Autowired(required = false) private val attachmentsService: AttachmentsService?,
    @param:Autowired(required = false) private val abilitiesFromOtherPermanentsService: AbilitiesFromOtherPermanentsService?
) {
    @get:JsonProperty
    var id: Int = 0
        get() = if (modifiers.permanentId > 0) modifiers.permanentId else field

    @JsonProperty
    var card: Card? = null

    @JsonProperty
    var owner: String? = null

    @get:JsonProperty
    var controller: String? = null
        get() {
            if (modifiers.modifiersUntilEndOfTurn.newController != null) {
                return modifiers.modifiersUntilEndOfTurn.newController
            }

            if (modifiers.controller != null) {
                return modifiers.controller
            }

            if (field != null) {
                return field
            }

            return owner
        }

    @JsonProperty
    var modifiers = CardModifiers()

    @JsonProperty
    var triggeredAbilities: List<Ability> = listOf()
    var acknowledgedBy: Set<String> = setOf()

    var gameStatus: GameStatus? = null

    @get:JsonProperty
    val power: Int
        get() {
            if (card!!.power == null) return 0
            return card!!.power!! +
                    modifiers.modifiersUntilEndOfTurn.extraPowerToughness.power +
                    modifiers.extraPowerToughnessFromCounters.power +
                    this.attachmentsPower +
                    this.powerFromOtherPermanents
        }

    @get:JsonProperty
    val toughness: Int
        get() {
            if (card!!.toughness == null) return 0
            return card!!.toughness!! +
                    modifiers.modifiersUntilEndOfTurn.extraPowerToughness.toughness +
                    modifiers.extraPowerToughnessFromCounters.toughness +
                    this.attachmentsToughness +
                    this.toughnessFromOtherPermanents
        }

    @get:JsonProperty
    val abilities: List<Ability>
        get() {
            return this.fixedAbilities + this.abilitiesFormOtherPermanents
        }

    @get:JsonProperty
    val isSummoningSickness: Boolean
        get() = modifiers.isSummoningSickness && !hasAbilityType(AbilityType.HASTE)

    @get:JsonProperty
    val isInstantSpeed: Boolean
        get() = card!!.types.contains(Type.INSTANT) || hasAbilityType(AbilityType.FLASH)

    @get:JsonProperty
    val isHistoric: Boolean
        get() = card!!.types.contains(Type.ARTIFACT) || card!!.types.contains(Type.LEGENDARY) || card!!.subtypes.contains(
            Subtype.SAGA
        )

    val idAndName: String
        get() = "\"" + this.id + " - " + card!!.name + "\""

    fun isOfType(type: Type): Boolean {
        return card!!.isOfType(type)
    }

    fun ofAnyOfTheTypes(types: List<Type>): Boolean {
        for (type in types) {
            if (isOfType(type)) {
                return true
            }
        }
        return false
    }

    fun ofAllOfTheTypes(types: List<Type>): Boolean {
        for (type in types) {
            if (!isOfType(type)) {
                return false
            }
        }
        return true
    }

    fun ofAnyOfTheSubtypes(subtypes: List<Subtype?>): Boolean {
        for (subtype in subtypes) {
            if (isOfSubtype(subtype)) {
                return true
            }
        }
        return false
    }

    fun isOfColor(color: Color): Boolean {
        return card!!.isOfColor(color)
    }

    fun ofAnyOfTheColors(colors: List<Color>): Boolean {
        for (color in colors) {
            if (isOfColor(color)) {
                return true
            }
        }
        return false
    }

    val isColorless: Boolean
        get() = card!!.isColorless()

    val isMulticolor: Boolean
        get() = card!!.isMulticolor()

    fun checkIfCanAttack() {
        if (!isOfType(Type.CREATURE)) {
            throw MessageException(this.idAndName + " is not of type Creature.")
        }

        if (modifiers.isTapped) {
            throw MessageException(this.idAndName + " is tapped and cannot attack.")
        }

        if (this.isSummoningSickness) {
            throw MessageException(this.idAndName + " has summoning sickness and cannot attack.")
        }

        if (hasAbilityType(AbilityType.DEFENDER)) {
            throw MessageException(this.idAndName + " has defender and cannot attack.")
        }
    }

    fun declareAsAttacker() {
        modifiers.isAttacking = true
    }

    fun declareAsBlocker(attackingCreatureId: Int) {
        modifiers.blockingCardId = attackingCreatureId
    }

    fun acknowledgedBy(playerName: String) {
        acknowledgedBy = acknowledgedBy + playerName
    }

    fun acknowledged(): Boolean {
        return acknowledgedBy.size == 2
    }

    fun notAcknowledged(): Boolean {
        return !acknowledged()
    }

    val fixedAbilities: List<Ability>
        get() {
            return abilities(card!!) +
                modifiers.abilities +
                modifiers.modifiersUntilEndOfTurn.extraAbilities +
                modifiers.counters.keywordCountersAbilities +
                this.attachmentsAbilities
        }

    fun canProduceMana(color: Color): Boolean {
        return getAbilitiesByTriggerType(TriggerType.MANA_ABILITY).stream()
            .flatMap<String?> { ability: Ability? -> ability!!.parameters.stream() }
            .anyMatch { parameter: String? -> parameter == color.toString() }
    }

    fun getAbilitiesByTriggerType(triggerType: TriggerType?): List<Ability> {
        return this.abilities.stream()
            .filter { ability: Ability? -> ability!!.trigger != null }
            .filter { ability: Ability? -> ability!!.trigger!!.type == triggerType }
            .collect(Collectors.toList())
    }

    fun getAbilitiesByTriggerSubType(triggerSubType: TriggerSubtype): List<Ability?> {
        return this.abilities.stream()
            .filter { ability: Ability? -> ability!!.trigger != null }
            .filter { ability: Ability? -> triggerSubType == ability!!.trigger!!.subtype }
            .collect(Collectors.toList())
    }

    fun getAbilitiesByType(abilityType: AbilityType?): List<Ability> =
        abilities.filter { it.abilityType == abilityType }

    fun hasAbilityType(abilityType: AbilityType?): Boolean =
        getAbilitiesByType(abilityType).isNotEmpty()

    fun getFixedAbilitiesByType(abilityType: AbilityType?): List<Ability?> {
        return this.fixedAbilities.stream()
            .filter { currentAbility: Ability? -> currentAbility!!.abilityType == abilityType }
            .collect(Collectors.toList())
    }

    fun hasFixedAbility(abilityType: AbilityType?): Boolean {
        return getFixedAbilitiesByType(abilityType).size > 0
    }

    fun hasAnyFixedAbility(abilityTypes: List<AbilityType?>): Boolean {
        return abilityTypes.stream()
            .anyMatch { abilityType: AbilityType? -> getFixedAbilitiesByType(abilityType).size > 0 }
    }

    fun hasFixedAbilityWithTriggerSubType(triggerSubtype: TriggerSubtype): Boolean {
        return getAbilitiesByTriggerSubType(triggerSubtype).size > 0
    }

    val isPermanent: Boolean
        get() = !(isOfType(Type.INSTANT) || isOfType(Type.SORCERY))

    fun isOfSubtype(subtype: Subtype?): Boolean {
        return this.card!!.subtypes.contains(subtype)
    }

    fun cleanup() {
        modifiers.cleanupUntilEndOfTurnModifiers()
        acknowledgedBy = setOf()
    }

    fun resetAllModifiers() {
        modifiers = CardModifiers()
    }

    private val attachmentsPower: Int
        get() = attachmentsService?.getAttachmentsPower(gameStatus!!, this) ?: 0

    private val attachmentsToughness: Int
        get() = attachmentsService?.getAttachmentsToughness(gameStatus!!, this) ?: 0

    private val attachmentsAbilities: List<Ability>
        get() = attachmentsService?.getAttachmentsAbilities(
            gameStatus!!,
            this
        ) ?: listOf()

    private val powerFromOtherPermanents: Int
        get() = abilitiesFromOtherPermanentsService?.getPowerFromOtherPermanents(
            gameStatus!!,
            this
        ) ?: 0

    private val toughnessFromOtherPermanents: Int
        get() = abilitiesFromOtherPermanentsService?.getToughnessFromOtherPermanents(
            gameStatus!!,
            this
        ) ?: 0

    private val abilitiesFormOtherPermanents: List<Ability>
        get() = abilitiesFromOtherPermanentsService?.getAbilitiesFormOtherPermanents(
            gameStatus!!,
            this
        )
            ?: listOf()
}
