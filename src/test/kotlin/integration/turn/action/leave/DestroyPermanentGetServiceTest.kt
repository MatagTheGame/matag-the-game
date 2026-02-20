package integration.turn.action.leave

import com.matag.cards.Cards
import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.leave.DestroyPermanentService
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
class DestroyPermanentGetServiceTest(
    private val destroyPermanentService: DestroyPermanentService,
    private val cardInstanceFactory: CardInstanceFactory,
    private val testUtils: TestUtils,
    private val cards: Cards
) {
    
    @Test
    fun testDestroy() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance =
            cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.player1!!.battlefield.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService.destroy(gameStatus, 61)

        // Then
        assertThat(destroyed).isTrue()
        assertThat(gameStatus.player1?.battlefield?.size()).isEqualTo(0)
        assertThat(gameStatus.player1?.graveyard?.cards).contains(cardInstance)
    }

    @Test
    fun testDestroyDontBlowUpIfCardsIsNotAnymoreInTheBattlefield() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance =
            cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.player1!!.graveyard.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService.destroy(gameStatus, 61)

        // Then
        assertThat(destroyed).isFalse()
        assertThat(gameStatus.player1?.battlefield?.size()).isEqualTo(0)
        assertThat(gameStatus.player1?.graveyard?.cards).contains(cardInstance)
    }

    @Test
    fun testDestroyIndestructibleDoesNotDestroyIt() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance =
            cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.extraAbilities += Ability(AbilityType.INDESTRUCTIBLE)
        gameStatus.player1!!.battlefield.addCard(cardInstance)

        // When
        val destroyed = destroyPermanentService.destroy(gameStatus, 61)

        // Then
        assertThat(destroyed).isFalse()
        assertThat(gameStatus.player1?.battlefield?.cards).contains(cardInstance)
        assertThat(gameStatus.player1?.graveyard?.size()).isEqualTo(0)
    }

    @Test
    fun testReduceToughnessToZeroOfIndestructibleDestroysIt() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val aCreature = cardInstanceFactory.create(
            gameStatus,
            61,
            cards.get("Seraph of the Suns"),
            "player-name",
            "player-name"
        )
        val instantMinus4 =
            cardInstanceFactory.create(gameStatus, 62, cards.get("Grasp of Darkness"), "player-name", "player-name")
        aCreature.modifiers.modifiersUntilEndOfTurn.extraAbilities += instantMinus4.abilities[0]
        gameStatus.player1!!.battlefield.addCard(aCreature)

        // When
        val destroyed = destroyPermanentService.destroy(gameStatus, 61)

        // Then
        assertThat(destroyed).isFalse()
        assertThat(gameStatus.player1?.battlefield?.cards).contains(aCreature)
        assertThat(gameStatus.player1?.graveyard?.size()).isEqualTo(0)
    }
}