package com.matag.game.launcher

import org.springframework.stereotype.Component

@Component
class LauncherGameResponseBuilder {
    fun build(session: String) =
        """
            <html>
                <head>
                    <meta charset="utf-8">
                    <title>MATAG - Game</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="shortcut icon" type="image/png" href="/matag/game/img/matag.png"/>
                    <script>sessionStorage.setItem('token', '$session')</script>
                </head>
                <body>
                    <div id="app"></div>
                    <script src="/matag/game/js/game.js" type="text/javascript"></script>
                </body>
            </html>
        """.trimIndent()
}
