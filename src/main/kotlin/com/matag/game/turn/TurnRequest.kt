package com.matag.game.turn

data class TurnRequest(
    var action: String? = null,
    var inputRequiredAction: String? = null,
    var inputRequiredActionParameter: String? = null,
    var inputRequiredChoices: String? = null,
    var mana: Map<Int, List<String>> = mapOf(),
    var cardIds: List<Int> = listOf(),
    var targetsIdsForCardIds: Map<Int, List<Any>> = mapOf(),
    var playedAbility: String? = null,
)
