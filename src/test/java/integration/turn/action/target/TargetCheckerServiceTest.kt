package integration.turn.action.target

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.target.TargetCheckerService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TargetTestConfiguration::class])
class TargetCheckerServiceTest {
    @Autowired
    private val targetCheckerService: TargetCheckerService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnAlwaysTrueForPlayer() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val sunscorchedDesert =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Sunscorched Desert"), "player-name")

        // When
        val isValid = targetCheckerService!!.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
            sunscorchedDesert,
            gameStatus
        )

        // Then
        Assertions.assertThat(isValid).isTrue()
    }

    @Test
    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnTrueIfThereIsSomethingToTarget() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val darkRemedy = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Dark Remedy"), "player-name")
        val aCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Banehound"), "opponent-name")
        gameStatus.getPlayer2().getBattlefield().addCard(aCreature)

        // When
        val isValid = targetCheckerService!!.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
            darkRemedy,
            gameStatus
        )

        // Then
        Assertions.assertThat(isValid).isTrue()
    }

    @Test
    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnFalseIfThereIsNothingToTarget() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val darkRemedy = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Dark Remedy"), "player-name")

        // When
        val isValid = targetCheckerService!!.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
            darkRemedy,
            gameStatus
        )

        // Then
        Assertions.assertThat(isValid).isFalse()
    }

    @Test
    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnFalseIfTargetHasHexproofAndTargetedByOpponent() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val darkRemedy = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Dark Remedy"), "player-name")
        val aCreatureWithHexproof =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Cold-Water Snapper"), "opponent-name")
        gameStatus.getPlayer2().getBattlefield().addCard(aCreatureWithHexproof)

        // When
        val isValid = targetCheckerService!!.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
            darkRemedy,
            gameStatus
        )

        // Then
        Assertions.assertThat(isValid).isFalse()
    }

    @Test
    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnTrueIfTargetHasHexproofAndTargetedByThePlayer() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val darkRemedy = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Dark Remedy"), "player-name")
        val aCreatureWithHexproof =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Cold-Water Snapper"), "player-name")
        gameStatus.getPlayer2().getBattlefield().addCard(aCreatureWithHexproof)

        // When
        val isValid = targetCheckerService!!.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
            darkRemedy,
            gameStatus
        )

        // Then
        Assertions.assertThat(isValid).isTrue()
    }
}
