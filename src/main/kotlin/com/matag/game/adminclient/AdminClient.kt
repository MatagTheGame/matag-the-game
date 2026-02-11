package com.matag.game.adminclient

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.matag.adminentities.DeckInfo
import com.matag.adminentities.FinishGameRequest
import com.matag.adminentities.PlayerInfo
import com.matag.game.config.ConfigService
import com.matag.game.player.Player
import com.matag.game.security.SecurityToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Profile("!test")
@Component
open class AdminClient(
    private val configService: ConfigService
) {
    open fun getDeckInfo(token: SecurityToken?): DeckInfo {
        return get(token, "/game/active-deck", DeckInfo::class.java)
    }

    open fun getPlayerInfo(token: SecurityToken?): PlayerInfo {
        return get(token, "/player/info", PlayerInfo::class.java)
    }

    fun finishGame(gameId: String?, winner: Player) {
        try {
            adminPost(
                "/game/$gameId/finish",
                FinishGameRequest(winner.token.adminToken),
                Any::class.java
            )
        } catch (e: Exception) {
            LOGGER.error("Could not finish the game.", e)
        }
    }

    private fun <T> get(token: SecurityToken?, url: String, responseType: Class<T>): T {
        return exchange(token, url, HttpMethod.GET, null, responseType)
    }

    private fun <T> adminGet(url: String?, responseType: Class<T>): T {
        return exchange(null, url, HttpMethod.GET, null, responseType)
    }

    private fun <T> post(token: SecurityToken?, url: String, body: Any, responseType: Class<T>): T {
        return exchange(token, url, HttpMethod.POST, body, responseType)
    }

    private fun <T> adminPost(url: String?, body: Any?, responseType: Class<T>): T {
        return exchange(null, url, HttpMethod.POST, body, responseType)
    }

    private fun <T> exchange(
        token: SecurityToken?,
        url: String?,
        method: HttpMethod,
        request: Any?,
        responseType: Class<T>
    ): T {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        if (token != null) {
            headers.set("session", token.adminToken)
        } else {
            headers.set("admin", configService.adminPassword)
        }

        val entity = HttpEntity(request, headers)

        val response = restTemplate.exchange(
            configService.matagAdminInternalUrl + url,
            method,
            entity,
            String::class.java
        )
        return OBJECT_MAPPER.readValue<T>(response.getBody(), responseType)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AdminClient::class.java)
        private val OBJECT_MAPPER: ObjectMapper =
            jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}
