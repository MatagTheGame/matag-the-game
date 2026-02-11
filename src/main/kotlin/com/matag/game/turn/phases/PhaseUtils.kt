package com.matag.game.turn.phases

import com.matag.game.turn.phases.beginning.UntapPhase
import com.matag.game.turn.phases.ending.CleanupPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase

object PhaseUtils {
    fun isMainPhase(phase: String): Boolean {
        return phase == Main1Phase.Companion.M1 || phase == Main2Phase.Companion.M2
    }

    fun isPriorityAllowed(phase: String): Boolean {
        return !(phase == UntapPhase.Companion.UT || phase == CleanupPhase.Companion.CL)
    }
}
