package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.BeginCombatPhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Tag("RegressionTests")
@Import(CastCreatureWithFlashGivingAbilitiesToOtherCreatures.InitTestServiceForTest::class)
class CastCreatureWithFlashGivingAbilitiesToOtherCreatures(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun testCreatureWithFlashGivingAbilitiesToOtherCreature() {
        // Going to combat
        browser.player1().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )

        // Attack with Ardenvale Paladin
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Ardenvale Paladin")).declareAsAttacker()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Block with Ancient Brontodon
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).declareAsBlocker()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(DeclareBlockersPhase.DB, PlayerType.PLAYER)

        // Playing Blacklance Paragon
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Blacklance Paragon")).click()
        browser.player1().getStackHelper().contains(cards.get("Blacklance Paragon"))
        browser.player1().getPhaseHelper().matches(DeclareBlockersPhase.DB, PlayerType.OPPONENT)

        // Opponent just accepts it
        browser.player2().getActionHelper().clickContinueAndExpectPhase(DeclareBlockersPhase.DB, PlayerType.PLAYER)

        // Player select Ardenvale Paladin as target
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getFirstCard(cards.get("Ardenvale Paladin")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(DeclareBlockersPhase.DB, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)

        // Ancient Brontodon dies because of the deathtouch and player gets 2 life
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).containsExactly(cards.get("Ardenvale Paladin"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).containsExactly(cards.get("Ancient Brontodon"))
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(22)
    }

    class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initTestGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Blacklance Paragon"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Ardenvale Paladin"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon"))
        }
    }
}
