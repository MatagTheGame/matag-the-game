package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import application.cast.CastCreatureAlternativeCostTest.InitTestServiceForTest
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CombatFlyingReachTest(
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
    fun combatFlyingReach() {
        // When going to combat
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )

        // creature with flying should have the correct class
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards!!.get("Air Elemental")).hasFlying()

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Declare blocker
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.COMBAT_LINE)
            .getCard(cards.get("Air Elemental"), 0).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).click()

        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.COMBAT_LINE)
            .getCard(cards.get("Air Elemental"), 1).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).click()

        val airElemental =
            browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.COMBAT_LINE)
                .getCard(cards.get("Air Elemental"), 2)
        airElemental.click()
        val ancientBrontodon =
            browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Ancient Brontodon"))
        ancientBrontodon.click()
        browser!!.player2().getMessageHelper().hasMessage("\"" + ancientBrontodon.getCardIdNumeric() + " - Ancient Brontodon\" cannot block \"" + airElemental.getCardIdNumeric() + " - Air Elemental\" as it has flying.")
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Grazing Whiptail"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Ancient Brontodon"))
        }
    }
}
