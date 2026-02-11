package integration.turn.action.selection

import com.matag.cards.Cards
import com.matag.cards.ability.selector.MagicInstanceSelector
import com.matag.cards.ability.selector.SelectorType
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.selection.MagicInstancePlayerSelectorService
import com.matag.player.PlayerType
import integration.TestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SelectionTestConfiguration::class])
class MagicInstancePlayerSelectorServiceTest(
    @param:Autowired val selectorService: MagicInstancePlayerSelectorService,
    @param:Autowired val cardInstanceFactory: CardInstanceFactory,
    @param:Autowired val testUtils: TestUtils,
    @param:Autowired val cards: Cards
) {
    @Test
    fun selectPlayer() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val magicInstanceSelector = MagicInstanceSelector(
                selectorType = SelectorType.PLAYER,
                itself = true
        )
        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name")

        // When
        val selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector)

        // Then
        assertThat(selection).containsExactly(gameStatus.player1)
    }

    @Test
    fun selectOpponent() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val magicInstanceSelector = MagicInstanceSelector(
                selectorType = SelectorType.PLAYER,
                controllerType = PlayerType.OPPONENT
        )
        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name")

        // When
        val selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector)

        // Then
        assertThat(selection).containsExactly(gameStatus.player2)
    }

    @Test
    fun selectAllPlayers() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = SelectorType.PLAYER,
            controllerType = PlayerType.PLAYER
        )
        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name")

        // When
        val selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector)

        // Then
        assertThat(selection).containsExactlyInAnyOrder(gameStatus.player1, gameStatus.getPlayer2())
    }
}