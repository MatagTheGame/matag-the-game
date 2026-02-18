package application.leave

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.combat.BeginCombatPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test

class CreatureDiesAbilityTest(
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
    fun creatureDiesAbility() {
        val firstGoblinId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getCard(cards.get("Goblin Assault Team"), 0).getCardIdNumeric()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Goblin Assault Team"), 1).getCardIdNumeric()

        // When opponent kills 1 goblin
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.OPPONENT)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Murder")).click()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).click()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.OPPONENT)

        // Then put +1/+1 counter is triggered
        browser.player1().getStackHelper().containsAbilitiesExactly(listOf("Player1's Goblin Assault Team ($firstGoblinId): That targets get 1 +1/+1 counters."))

        // When clicking on the other goblin
        browser.player2().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.PLAYER)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).target()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.PLAYER)

        // Then that goblin gets a counter
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).hasPlus1Counters(1)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).hasPowerAndToughness("5/2")
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Goblin Assault Team"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Goblin Assault Team"))

            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Murder"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
        }
    }
}
