package com.matag.game.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class ConfigService(
    @param:Value("\${matag.admin.path}")
    val matagAdminPath: String,

    @param:Value("\${matag.admin.internal.url}")
    val matagAdminInternalUrl: String,

    @param:Value("\${matag.admin.password}")
    val adminPassword: String
)
