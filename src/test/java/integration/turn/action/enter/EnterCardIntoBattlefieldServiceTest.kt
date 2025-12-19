package integration.turn.action.enter

import com.matag.cards.Cards
import com.matag.cards.ability.Ability
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.action.player.DrawXCardsService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Map

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [EnterTestConfiguration::class])
class EnterCardIntoBattlefieldServiceTest {
    @Autowired
    private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val drawXCardsService: DrawXCardsService? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun enterTheBattlefield() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Swamp"), "player-name")
        card.setController("player-name")

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Assertions.assertThat(card.getModifiers().getPermanentId()).isGreaterThan(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getBattlefield().getCards()).contains(card)
    }

    @Test
    fun enterTheBattlefieldOpponent() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Swamp"), "opponent-name")
        card.setController("opponent-name")

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Assertions.assertThat(card.getModifiers().getPermanentId()).isGreaterThan(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer2().getBattlefield().getCards()).contains(card)
    }

    @Test
    fun enterTheBattlefieldTapped() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Diregraf Ghoul"), "player-name")
        card.setController("player-name")

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        card.getModifiers().isTapped()
    }

    @Test
    fun enterTheBattlefieldTrigger() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Jadecraft Artisan"), "player-name")
        card.setController("player-name")

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getStack().getItems()).contains(card)
        Assertions.assertThat<Ability?>(card.getTriggeredAbilities()).hasSize(1)
        Assertions.assertThat(card.getTriggeredAbilities().get(0).abilityTypeText).isEqualTo("That targets get +2/+2.")
    }

    @Test
    fun enterTheBattlefieldAdamantTriggered() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Ardenvale Paladin"), "player-name")
        card.setController("player-name")

        gameStatus.getTurn().setLastManaPaid(
            Map.of<Int?, MutableList<String?>?>(
                1, mutableListOf<String?>("WHITE"),
                2, mutableListOf<String?>("WHITE"),
                3, mutableListOf<String?>("WHITE"),
                4, mutableListOf<String?>("BLUE")
            )
        )

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Assertions.assertThat(card.getModifiers().getCounters().getPlus1Counters()).isEqualTo(1)
    }

    @Test
    fun enterTheBattlefieldAdamantNotTriggered() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Ardenvale Paladin"), "player-name")
        card.setController("player-name")

        gameStatus.getTurn().setLastManaPaid(
            Map.of<Int?, MutableList<String?>?>(
                1, mutableListOf<String?>("WHITE"),
                2, mutableListOf<String?>("WHITE"),
                3, mutableListOf<String?>("BLUE"),
                4, mutableListOf<String?>("BLUE")
            )
        )

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Assertions.assertThat(card.getModifiers().getCounters().getPlus1Counters()).isEqualTo(0)
    }

    @Test
    fun enterTheBattlefieldAdamantSameTriggered() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Clockwork Servant"), "player-name")
        card.setController("player-name")

        gameStatus.getTurn().setLastManaPaid(
            Map.of<Int?, MutableList<String?>?>(
                1, mutableListOf<String?>("WHITE"),
                2, mutableListOf<String?>("WHITE"),
                3, mutableListOf<String?>("BLUE"),
                4, mutableListOf<String?>("BLUE")
            )
        )

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Mockito.verifyNoMoreInteractions(drawXCardsService)
    }

    @Test
    fun enterTheBattlefieldAdamantSameNotTriggered() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Clockwork Servant"), "player-name")
        card.setController("player-name")

        gameStatus.getTurn().setLastManaPaid(
            Map.of<Int?, MutableList<String?>?>(
                1, mutableListOf<String?>("BLACK"),
                2, mutableListOf<String?>("BLACK"),
                3, mutableListOf<String?>("BLUE"),
                4, mutableListOf<String?>("BLACK")
            )
        )

        // When
        enterCardIntoBattlefieldService!!.enter(gameStatus, card)

        // Then
        Mockito.verify<DrawXCardsService?>(drawXCardsService).drawXCards(gameStatus.getPlayer1(), 1, gameStatus)
    }
}
