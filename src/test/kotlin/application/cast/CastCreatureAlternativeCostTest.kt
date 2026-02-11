package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.cards.properties.Color
import com.matag.game.status.GameStatus
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@Tag("RegressionTests")
class CastCreatureAlternativeCostTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castCreatureAlternativeCost() {
        // When click on creature without paying the cost
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards!!.get("Headwater Sentries")).click()

        // Then stack is still empty
        browser.player1().stackHelper.toHaveSize(0)

        // When clicking the dual land
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).click()

        // Then it's possible to chose which mana generate
        browser.player1().playableAbilitiesHelper.toHaveAbilities(
            mutableListOf<String?>(
                "Tap add white mana.",
                "Tap add blue mana."
            )
        )

        // When clicking on the WHITE
        browser.player1().playableAbilitiesHelper.choose(0)

        // Then WHITE mana is generated
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).isFrontendTapped
        browser.player1().playerActiveManaHelper.toHaveMana(mutableListOf<Color?>(Color.WHITE))

        // When clicking on other lands and try to play the creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Headwater Sentries")).click()

        // Then the creature is not played
        browser.player1().getHandHelper(PlayerType.PLAYER).contains(cards.get("Headwater Sentries"))

        // When clicking the dual land
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).click()

        // The dual land gets untapped
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).isNotFrontendTapped
        browser.player1().playerActiveManaHelper.toHaveMana(
            Arrays.asList<Color?>(
                Color.GREEN,
                Color.GREEN,
                Color.GREEN
            )
        )

        // Untapping 2 creatures
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 1).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 1).isNotFrontendTapped
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 2).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Druid of the Cowl"), 2).isNotFrontendTapped

        // When clicking the dual land for BLUE
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).click()
        browser.player1().playableAbilitiesHelper.choose(1)

        // Then BLUE mana is generated
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards.get("Azorius Guildgate")).isFrontendTapped
        browser.player1().playerActiveManaHelper.toHaveMana(Arrays.asList<Color?>(Color.BLUE, Color.GREEN))

        // When cards.get("Gyre Engineer") is clicked
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Gyre Engineer")).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Gyre Engineer")).isFrontendTapped

        // Then 2 mana are generated
        browser.player1().playerActiveManaHelper.toHaveMana(
            Arrays.asList<Color?>(
                Color.BLUE,
                Color.BLUE,
                Color.GREEN,
                Color.GREEN
            )
        )

        // When clicking on the creature
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Headwater Sentries")).click()

        // creature is now played
        browser.player1().stackHelper.contains(cards.get("Headwater Sentries"))
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Headwater Sentries"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Azorius Guildgate"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Gyre Engineer"))
        }
    }
}
