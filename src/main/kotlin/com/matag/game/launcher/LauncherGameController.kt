package com.matag.game.launcher

import com.matag.game.config.ConfigService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class LauncherGameController(
    private val launcherGameResponseBuilder: LauncherGameResponseBuilder,
    private val configService: ConfigService
) {
    @ResponseBody
    @PostMapping(path = ["/ui/{id}"])
    fun launchGame(httpServletRequest: HttpServletRequest): String {
        val session = httpServletRequest.getParameter("session")
        validateSessionFormat(session)

        return launcherGameResponseBuilder.build(session)
    }

    @GetMapping(path = ["/ui/{id}"])
    fun onRefresh(): RedirectView {
        return RedirectView(configService.matagAdminPath + "/play")
    }

    private fun validateSessionFormat(session: String) {
        UUID.fromString(session)
    }
}
