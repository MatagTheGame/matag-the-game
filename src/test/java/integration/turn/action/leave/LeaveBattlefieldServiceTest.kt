package integration.turn.action.leave

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.attach.AttachService
import com.matag.game.turn.action.leave.LeaveBattlefieldService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LeaveTestConfiguration::class])
class LeaveBattlefieldServiceTest {
    @Autowired
    private val leaveBattlefieldService: LeaveBattlefieldService? = null

    @Autowired
    private val attachService: AttachService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun testLeaveBattlefield() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.getModifiers().setTapped(true)
        gameStatus.getPlayer1().getBattlefield().addCard(cardInstance)

        // When
        leaveBattlefieldService!!.leaveTheBattlefield(gameStatus, 61)

        // Then
        Assertions.assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0)
        Assertions.assertThat(cardInstance.getModifiers().isTapped()).isFalse()
    }

    @Test
    fun testLeaveBattlefieldWithAttachments() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val creature =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.getPlayer1().getBattlefield().addCard(creature)

        val enchantment1 =
            cardInstanceFactory.create(gameStatus, 62, cards.get("Knight's Pledge"), "player-name", "player-name")
        gameStatus.getPlayer1().getBattlefield().addCard(enchantment1)
        attachService!!.attach(gameStatus, enchantment1, creature.getId())

        val enchantment2 = cardInstanceFactory.create(
            gameStatus,
            63,
            cards.get("Knight's Pledge"),
            "opponent-name",
            "opponent-name"
        )
        gameStatus.getPlayer2().getBattlefield().addCard(enchantment2)
        attachService.attach(gameStatus, enchantment2, creature.getId())

        val equipment =
            cardInstanceFactory.create(gameStatus, 64, cards.get("Marauder's Axe"), "player-name", "player-name")
        gameStatus.getPlayer1().getBattlefield().addCard(equipment)
        attachService.attach(gameStatus, equipment, creature.getId())

        // When
        leaveBattlefieldService!!.leaveTheBattlefield(gameStatus, 61)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getBattlefield().getCards())
            .containsExactlyInAnyOrder(equipment, enchantment1)
        Assertions.assertThat(enchantment1.getModifiers().getModifiersUntilEndOfTurn().isToBeDestroyed()).isTrue()
        Assertions.assertThat(equipment.getModifiers().getAttachedToId()).isEqualTo(0)
    }
}