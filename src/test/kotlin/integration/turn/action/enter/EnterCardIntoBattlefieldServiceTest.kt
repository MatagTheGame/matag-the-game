package integration.turn.action.enter

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.action.player.DrawXCardsService
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
class EnterCardIntoBattlefieldServiceTest(
    private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService,
    private val cardInstanceFactory: CardInstanceFactory,
    private val testUtils: TestUtils,
    private val cards: Cards
) {

    @Test
    fun enterTheBattlefield() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name")
        card.controller = "player-name"

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(card.modifiers.permanentId).isGreaterThan(0)
        assertThat(gameStatus.player1?.battlefield?.cards).contains(card)
    }

    @Test
    fun enterTheBattlefieldOpponent() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "opponent-name")
        card.controller = "opponent-name"

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(card.modifiers.permanentId).isGreaterThan(0)
        assertThat(gameStatus.player2!!.battlefield.cards).contains(card)
    }

    @Test
    fun enterTheBattlefieldTapped() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Diregraf Ghoul"), "player-name")
        card.controller = "player-name"

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(card.modifiers.isTapped).isTrue()
    }

    @Test
    fun enterTheBattlefieldTrigger() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Jadecraft Artisan"), "player-name")
        card.controller = "player-name"

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(gameStatus.stack.items).contains(card)
        assertThat(card.triggeredAbilities).hasSize(1)
        assertThat(card.triggeredAbilities.get(0).abilityTypeText).isEqualTo("That targets get +2/+2.")
    }

    @Test
    fun enterTheBattlefieldAdamantTriggered() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Ardenvale Paladin"), "player-name")
        card.controller = "player-name"

        gameStatus.turn.lastManaPaid =
            mapOf(
                1 to listOf("WHITE"),
                2 to listOf("WHITE"),
                3 to listOf("WHITE"),
                4 to listOf("BLUE")
            )

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(card.modifiers.counters.plus1Counters).isEqualTo(1)
    }

    @Test
    fun enterTheBattlefieldAdamantNotTriggered() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Ardenvale Paladin"), "player-name")
        card.controller = "player-name"

        gameStatus.turn.lastManaPaid =
            mapOf(
                1 to listOf("WHITE"),
                2 to listOf("WHITE"),
                3 to listOf("BLUE"),
                4 to listOf("BLUE")
            )

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(card.modifiers.counters.plus1Counters).isEqualTo(0)
    }

    @Test
    fun enterTheBattlefieldAdamantSameNotTriggered() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Clockwork Servant"), "player-name")
        card.controller = "player-name"

        gameStatus.turn.lastManaPaid =
            mapOf(
                1 to listOf("WHITE"),
                2 to listOf("WHITE"),
                3 to listOf("BLUE"),
                4 to listOf("BLUE")
            )

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(gameStatus.player1!!.hand.size()).isEqualTo(7)
    }

    @Test
    fun enterTheBattlefieldAdamantSameTriggered() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Clockwork Servant"), "player-name")
        card.controller = "player-name"

        gameStatus.turn.lastManaPaid =
            mapOf(
                1 to listOf("BLACK"),
                2 to listOf("BLACK"),
                3 to listOf("BLUE"),
                4 to listOf("BLACK")
            )

        // When
        enterCardIntoBattlefieldService.enter(gameStatus, card)

        // Then
        assertThat(gameStatus.player1!!.hand.size()).isEqualTo(8)
    }
}
