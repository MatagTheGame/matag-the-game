package integration.cardinstance

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.message.MessageException
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class CardInstanceTest(
    @param:Autowired val cardInstanceFactory: CardInstanceFactory,
    @param:Autowired val cards: Cards,
    @param:Autowired val testUtils: TestUtils
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