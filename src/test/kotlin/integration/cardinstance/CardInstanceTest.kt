package integration.cardinstance

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.message.MessageException
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class CardInstanceTest(
    val cardInstanceFactory: CardInstanceFactory,
    val cards: Cards,
    val testUtils: TestUtils
) {
    @Test
    fun checkIfCanAttackShouldWorkForCreatures() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = testUtils.createCardInstance(gameStatus, "Feral Maaka")

        // When
        cardInstance.checkIfCanAttack()
    }

    @Test
    fun checkIfCanAttackShouldThrowExceptionForNonCreatures() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val cardInstance = testUtils.createCardInstance(gameStatus, "Short Sword");

        // Expects
        assertThatThrownBy { cardInstance.checkIfCanAttack() }
            .isInstanceOf(MessageException::class.java)
            .hasMessage("\"${cardInstance.id} - Short Sword\" is not of type Creature.")
    }

    @Test
    fun checkIfCanAttackShouldThrowExceptionForTappedCreatures() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val cardInstance = testUtils.createCardInstance(gameStatus, "Feral Maaka");
        cardInstance.modifiers.isTapped = true;

        // Expects
        assertThatThrownBy { cardInstance.checkIfCanAttack() }
            .isInstanceOf(MessageException::class.java)
            .hasMessage("\"${cardInstance.id} - Feral Maaka\" is tapped and cannot attack.")
    }

    @Test
    fun checkIfCanAttackShouldThrowExceptionForCreaturesWithSummoningSickness() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val cardInstance = testUtils.createCardInstance(gameStatus, "Feral Maaka");
        cardInstance.modifiers.isSummoningSickness = true;

        // Expects
        assertThatThrownBy { cardInstance.checkIfCanAttack() }
            .isInstanceOf(MessageException::class.java)
            .hasMessage("\"${cardInstance.id} - Feral Maaka\" has summoning sickness and cannot attack.")
    }

    @Test
    fun checkIfCanAttackShouldBeOkForCreaturesWithSummoningSicknessButHaste() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = testUtils.createCardInstance(gameStatus, "Nest Robber")
        cardInstance.modifiers.isSummoningSickness = true

        // When
        cardInstance.checkIfCanAttack()
    }

    @Test
    fun checkIfCanAttackShouldThrowExceptionForCreaturesWithDefender() {
        // Given
        val gameStatus = testUtils.testGameStatus();
        val cardInstance = testUtils.createCardInstance(gameStatus, "Guardians of Meletis");

        // Expects
        assertThatThrownBy { cardInstance.checkIfCanAttack() }
            .isInstanceOf(MessageException::class.java)
            .hasMessage("\"${cardInstance.id} - Guardians of Meletis\" has defender and cannot attack.")
    }
}