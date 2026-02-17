package application.equip

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import application.cast.CastCreatureAlternativeCostTest.InitTestServiceForTest
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CastEquipmentDestroyCreatureTest(
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
    fun castEquipmentDestroyCreature() {
        // When cast an artifact equipment
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Short Sword")).click()

        // Equipment goes on the stack
        browser.player1().getStackHelper().containsExactly(cards.get("Short Sword"))

        // When opponent accepts equipment
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Short Sword"))
        val shortSwordId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Short Sword")).getCardIdNumeric()

        // When equipping
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Short Sword")).select()
        browser.player1().getStatusHelper().hasMessage("Select targets for Short Sword.")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Prowling Caracal")).target()

        // Equip ability goes on the stack
        browser.player1().getStackHelper().containsAbility("Player1's Short Sword (" + shortSwordId + "): Equipped creature gets +1/+1.")

        // When opponent accepts the equip
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then the target creature is equipped
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .containsExactly(cards.get("Prowling Caracal"), cards.get("Short Sword"))
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Prowling Caracal")).hasPowerAndToughness("4/2")

        // Destroy the creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 4).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).select()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Prowling Caracal")).target()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Creature is in the graveyard
        browser.player1().getGraveyardHelper(PlayerType.PLAYER)
            .contains(cards.get("Prowling Caracal"), cards.get("Legion's Judgment"))

        // Creature is still in the battlefield
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Short Sword"))
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Prowling Caracal"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Short Sword"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
        }
    }
}
