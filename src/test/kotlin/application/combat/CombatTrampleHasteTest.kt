package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("RegressionTests")
class CombatTrampleHasteTest(
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
    fun combatTrampleHaste() {
        // Playing card with trample haste
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 3).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 4).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Charging Monstrosaur")).click()
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // When going to combat
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).declareAsAttacker()
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Declare blocker
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.COMBAT_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).declareAsBlocker()
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)

        // Then
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(17)

        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Huatli's Snubhorn"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).hasDamage(2)
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Charging Monstrosaur"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Huatli's Snubhorn"))
        }
    }
}
