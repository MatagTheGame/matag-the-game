package com.matag.game.player

import com.matag.game.cardinstance.CardListComponent
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class Graveyard : CardListComponent()
