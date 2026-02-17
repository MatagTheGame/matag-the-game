package application.cleanup

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import application.cast.CastCreatureAlternativeCostTest.InitTestServiceForTest
import com.matag.cards.Cards
import com.matag.cards.properties.PowerToughness
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CleanupTest(
    adminClient: AdminClient,
    gameStatusRepository: GameStatusRepository,
    cardInstanceFactory: CardInstanceFactory,
    cards: Cards,
    private var initService: InitTestService,
) : AbstractApplicationTest(adminClient, gameStatusRepository, cardInstanceFactory, cards) {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun cleanupWhenPassingTurns() {
        // Battlefields is
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards!!.get("Huatli's Snubhorn")).hasDamage(1)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).isTapped()

        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasSummoningSickness()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("4/5")

        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).hasDamage(1)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).isTapped()

        // Phase is
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)

        // Player1 goes to M2
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)

        // Player1 passes the turn
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)

        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).doesNotHaveDamage()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).isTapped()

        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasSummoningSickness()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4")

        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).isNotTapped()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).doesNotHaveDamage()
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            // Current Player
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"))

            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"))

            val huatlisShubhorn: CardInstance = gameStatus.currentPlayer.battlefield.cards.get(0)
            huatlisShubhorn.modifiers.isTapped = true
            huatlisShubhorn.modifiers.dealDamage(1)

            val grazingWhiptail: CardInstance = gameStatus.currentPlayer.battlefield.cards.get(1)
            grazingWhiptail.modifiers.isSummoningSickness = true
            grazingWhiptail.modifiers.modifiersUntilEndOfTurn
                .addExtraPowerToughnessUntilEndOfTurn(PowerToughness(1, 1))

            // Non Current Player
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"))
            val airElemental: CardInstance = gameStatus.nonCurrentPlayer.battlefield.cards.get(0)
            airElemental.modifiers.dealDamage(1)
            airElemental.modifiers.isTapped = true
        }
    }
}
