package com.matag.game.turn.phases

import org.springframework.stereotype.Component

@Component
class PhaseFactory(
    private val phases: List<Phase>
) {

    fun get(phaseName: String): Phase =
        phases.firstOrNull { it.name == phaseName } ?: throw UnsupportedOperationException("Phase $phaseName not valid")
}
