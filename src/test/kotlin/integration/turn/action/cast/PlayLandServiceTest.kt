package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.PlayLandService
import com.matag.game.turn.phases.combat.FirstStrikePhase.Companion.FS
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class PlayLandServiceTest(
    private val playLandService: PlayLandService,
    private val cardInstanceFactory: CardInstanceFactory,
    private val testUtils: TestUtils,
    private val cards: Cards
) {

    @Test
    fun playLand() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name")
        gameStatus.player1!!.hand.addCard(card)

        // When
        playLandService.playLand(gameStatus, card.id)

        // Then
        assertThat(gameStatus.player1!!.battlefield.cards).contains(card)
    }

    @Test
    fun playLandErrorNotALand() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Befuddle"), "player-name");
        gameStatus.player1!!.hand.addCard(card);

        // When
        val exception = assertThrows<Exception> { playLandService.playLand(gameStatus, card.id) }

        // Then
        assertThat(exception).hasMessage("Playing \"100 - Befuddle\" as land.")
    }

    @Test
    fun playLandMultipleLand() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val card1 = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
        gameStatus.player1!!.hand.addCard(card1);
        val card2 = cardInstanceFactory.create(gameStatus, 101, cards.get("Swamp"), "player-name");
        gameStatus.player1!!.hand.addCard(card2);

        // When
        playLandService.playLand(gameStatus, card1.id);
        val exception = assertThrows<Exception> { playLandService.playLand(gameStatus, card2.id) }

        // Then
        assertThat(exception).hasMessage("You already played a land this turn.")
    }

    @Test
    fun playLandNotInMainPhase() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        gameStatus.turn.currentPhase = FS;
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
        gameStatus.player1!!.hand.addCard(card);

        // When
        val exception = assertThrows<Exception> { playLandService.playLand(gameStatus, card.id) }

        // Then
        assertThat(exception).hasMessage("You can only play lands during main phases.")
    }
}