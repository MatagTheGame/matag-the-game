package integration.turn.action._continue

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.beginning.UpkeepPhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.ending.CleanupPhase
import integration.TestUtils
import integration.turn.action.leave.LeaveTestConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ContinueTestConfiguration::class, LeaveTestConfiguration::class])
class AutocontinueCheckerTest {
    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val autocontinueChecker: AutocontinueChecker? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Test
    fun canPerformAnyActionReturnsTrueIfInputRequiredAction() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(CleanupPhase.CL)
        gameStatus.turn.setInputRequiredAction("InputRequiredAction")
        gameStatus.getPlayer1().hand.cards.clear()

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndNoCardsInHandOrBattlefield() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsTrueIfUPAndAffordableInstantInHand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()
        gameStatus.getPlayer1().battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                61,
                cards!!.get("Mountain"),
                "player-name",
                "player-name"
            )
        )
        gameStatus.getPlayer1().hand
            .addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name"))

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndNotAffordableInstantInHand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()
        gameStatus.getPlayer1().battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                61,
                cards!!.get("Mountain"),
                "player-name",
                "player-name"
            )
        )
        gameStatus.getPlayer1().battlefield.cards.get(0).modifiers.setTapped(true)
        gameStatus.getPlayer1().hand
            .addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name"))

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndCardWithTriggeredAbility() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory!!.create(gameStatus, 62, cards!!.get("Exclusion Mage"), "player-name"))

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfUPAndCardWithNotAffordableActivatedAbility() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory!!.create(gameStatus, 62, cards!!.get("Locthwain Gargoyle"), "player-name"))

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun canPerformAnyActionReturnsTrueIfUPAndCardAffordableActivatedAbility() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(UpkeepPhase.UP)
        gameStatus.getPlayer1().hand.cards.clear()
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory!!.create(gameStatus, 62, cards!!.get("Locthwain Gargoyle"), "player-name"))
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory.create(gameStatus, 63, cards.get("Plains"), "player-name"))
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory.create(gameStatus, 64, cards.get("Plains"), "player-name"))
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory.create(gameStatus, 65, cards.get("Plains"), "player-name"))
        gameStatus.getPlayer1().battlefield
            .addCard(cardInstanceFactory.create(gameStatus, 66, cards.get("Plains"), "player-name"))

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsTrueEvenIfDAButSomethingIsOnTheStackUnacknowledged() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.getPlayer1().hand.cards.clear()
        val brazenWolves = cardInstanceFactory!!.create(gameStatus, 62, cards!!.get("Brazen Wolves"), "player-name")

        brazenWolves.acknowledgedBy("player1")
        gameStatus.stack.push(brazenWolves)

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canPerformAnyActionReturnsFalseIfDAAndSomethingIsOnTheStackAcknowledgedByBothPlayers() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.getPlayer1().hand.cards.clear()
        val brazenWolves = cardInstanceFactory!!.create(gameStatus, 62, cards!!.get("Brazen Wolves"), "player-name")

        brazenWolves.acknowledgedBy("player1")
        brazenWolves.acknowledgedBy("player2")
        gameStatus.stack.push(brazenWolves)

        // When
        val result = autocontinueChecker!!.canPerformAnyAction(gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }
}