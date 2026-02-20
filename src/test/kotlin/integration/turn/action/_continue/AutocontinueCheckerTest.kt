package integration.turn.action._continue

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.beginning.UpkeepPhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.ending.CleanupPhase
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
class AutocontinueCheckerTest(
    private val testUtils: TestUtils,
    private val autocontinueChecker: AutocontinueChecker,
    private val cards: Cards,
    private val cardInstanceFactory: CardInstanceFactory
) {

    @Test
    fun canPerformAnyActionReturnsTrueIfInputRequiredAction() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = CleanupPhase.CL
        gameStatus.turn.inputRequiredAction = "InputRequiredAction"
        gameStatus.player1?.hand?.cards = listOf()

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndNoCardsInHandOrBattlefield() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsTrueIfUPAndAffordableInstantInHand() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()
        gameStatus.player1?.battlefield?.addCard(
            cardInstanceFactory.create(
                gameStatus,
                61,
                cards.get("Mountain"),
                "player-name",
                "player-name"
            )
        )
        gameStatus.player1?.hand?.addCard(
            cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name")
        )

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndNotAffordableInstantInHand() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()
        gameStatus.player1?.battlefield?.addCard(
            cardInstanceFactory.create(
                gameStatus,
                61,
                cards.get("Mountain"),
                "player-name",
                "player-name"
            )
        )
        gameStatus.player1?.battlefield?.cards?.get(0)?.modifiers?.isTapped = true
        gameStatus.player1?.hand?.addCard(
            cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name")
        )

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndCardWithTriggeredAbility() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()
        gameStatus.player1?.battlefield?.addCard(
            cardInstanceFactory.create(gameStatus, 62, cards.get("Exclusion Mage"), "player-name")
        )

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndCardWithNotAffordableActivatedAbility() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()
        gameStatus.player1?.battlefield?.addCard(
            cardInstanceFactory.create(gameStatus, 62, cards.get("Locthwain Gargoyle"), "player-name")
        )

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsTrueIfUPAndCardAffordableActivatedAbility() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = UpkeepPhase.UP
        gameStatus.player1?.hand?.cards = listOf()
        gameStatus.player1?.battlefield?.addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Locthwain Gargoyle"), "player-name"))
        gameStatus.player1?.battlefield?.addCard(cardInstanceFactory.create(gameStatus, 63, cards.get("Plains"), "player-name"))
        gameStatus.player1?.battlefield?.addCard(cardInstanceFactory.create(gameStatus, 64, cards.get("Plains"), "player-name"))
        gameStatus.player1?.battlefield?.addCard(cardInstanceFactory.create(gameStatus, 65, cards.get("Plains"), "player-name"))
        gameStatus.player1?.battlefield?.addCard(cardInstanceFactory.create(gameStatus, 66, cards.get("Plains"), "player-name"))

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsTrueEvenIfDAButSomethingIsOnTheStackUnacknowledged() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.player1?.hand?.cards = listOf()
        val brazenWolves = cardInstanceFactory.create(gameStatus, 62, cards.get("Brazen Wolves"), "player-name")

        brazenWolves.acknowledgedBy("player1")
        gameStatus.stack.push(brazenWolves)

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfDAAndSomethingIsOnTheStackAcknowledgedByBothPlayers() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.player1?.hand?.cards = listOf()
        val brazenWolves = cardInstanceFactory.create(gameStatus, 62, cards.get("Brazen Wolves"), "player-name")

        brazenWolves.acknowledgedBy("player1")
        brazenWolves.acknowledgedBy("player2")
        gameStatus.stack.push(brazenWolves)

        // When
        val result = autocontinueChecker.canPerformAnyAction(gameStatus)

        // Then
        assertThat(result).isFalse()
    }
}