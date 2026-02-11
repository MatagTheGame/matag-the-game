package com.matag.game.security

data class SecurityToken(
    val sessionId: String,
    val adminToken: String,
    val gameId: String
) {
    override fun toString(): String {
        return "SecurityToken{sessionId='********', adminToken='********', gameId='$gameId'}"
    }
}
