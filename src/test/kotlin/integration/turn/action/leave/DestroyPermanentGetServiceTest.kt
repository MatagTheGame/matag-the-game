package integration.turn.action.leave

import com.matag.cards.Cards
import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.leave.DestroyPermanentService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LeaveTestConfiguration::class])
class DestroyPermanentGetServiceTest {
    @Autowired
    private val destroyPermanentService: DestroyPermanentService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun testDestroy() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.getPlayer1().battlefield.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService!!.destroy(gameStatus, 61)

        // Then
        Assertions.assertThat(destroyed).isTrue()
        Assertions.assertThat(gameStatus.getPlayer1().battlefield.size()).isEqualTo(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().graveyard.cards).contains(cardInstance)
    }

    @Test
    fun testDestroyDontBlowUpIfCardsIsNotAnymoreInTheBattlefield() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.getPlayer1().graveyard.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService!!.destroy(gameStatus, 61)

        // Then
        Assertions.assertThat(destroyed).isFalse()
        Assertions.assertThat(gameStatus.getPlayer1().battlefield.size()).isEqualTo(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().graveyard.cards).contains(cardInstance)
    }

    @Test
    fun testDestroyIndestructibleDoesNotDestroyIt() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.getExtraAbilities()
            .add(Ability(AbilityType.INDESTRUCTIBLE))
        gameStatus.getPlayer1().battlefield.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService!!.destroy(gameStatus, 61)

        // Then
        Assertions.assertThat(destroyed).isFalse()
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().battlefield.cards).contains(cardInstance)
        Assertions.assertThat(gameStatus.getPlayer1().graveyard.size()).isEqualTo(0)
    }

    @Test
    fun testReduceToughnessToZeroOfIndestructibleDestroysIt() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val aCreature = cardInstanceFactory!!.create(
            gameStatus,
            61,
            cards!!.get("Seraph of the Suns"),
            "player-name",
            "player-name"
        )
        val instantMinus4 =
            cardInstanceFactory.create(gameStatus, 62, cards.get("Grasp of Darkness"), "player-name", "player-name")
        aCreature.modifiers.modifiersUntilEndOfTurn.getExtraAbilities()
            .add(instantMinus4.getAbilities().get(0))
        gameStatus.getPlayer1().battlefield.addCard(aCreature)

        // When
        val destroyed = destroyPermanentService!!.destroy(gameStatus, 61)

        // Then
        Assertions.assertThat(destroyed).isFalse()
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().battlefield.cards).contains(aCreature)
        Assertions.assertThat(gameStatus.getPlayer1().graveyard.size()).isEqualTo(0)
    }
}