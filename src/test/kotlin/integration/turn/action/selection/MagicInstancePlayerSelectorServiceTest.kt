package integration.turn.action.selection

import com.matag.cards.Cards
import com.matag.cards.ability.selector.MagicInstanceSelector
import com.matag.cards.ability.selector.SelectorType
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.selection.MagicInstancePlayerSelectorService
import com.matag.player.PlayerType
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class MagicInstancePlayerSelectorServiceTest(
    val selectorService: MagicInstancePlayerSelectorService,
    val cardInstanceFactory: CardInstanceFactory,
    val testUtils: TestUtils,
    val cards: Cards
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
        assertThat(selection).containsExactlyInAnyOrder(gameStatus.player1, gameStatus.player2)
    }
}