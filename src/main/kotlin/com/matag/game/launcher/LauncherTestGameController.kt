package com.matag.game.launcher

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.atomic.AtomicLong

@Profile("test")
@Controller
open class LauncherTestGameController(
    private val launcherGameResponseBuilder: LauncherGameResponseBuilder
) {

    @ResponseBody
    @GetMapping(path = ["/ui/test-game"])
    open fun launchGame(): String {
        return launcherGameResponseBuilder.build(TEST_ADMIN_TOKEN.incrementAndGet().toString())
    }

    companion object {
        val TEST_ADMIN_TOKEN: AtomicLong = AtomicLong()
    }
}
