package integration.turn.action.combat

import com.matag.cards.Cards
import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.InputRequiredActions
import com.matag.game.turn.action.combat.CombatService
import com.matag.game.turn.action.combat.DeclareAttackerService
import com.matag.game.turn.action.combat.DeclareBlockerService
import com.matag.game.turn.action.damage.DealDamageToCreatureService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.player.LifeService
import com.matag.game.turn.action.trigger.WhenTriggerService
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.combat.FirstStrikePhase
import integration.TestUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Map

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CombatTestConfiguration::class])
class CombatServiceTest {
    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val combatService: CombatService? = null

    @Autowired
    private val declareAttackerService: DeclareAttackerService? = null

    @Autowired
    private val declareBlockerService: DeclareBlockerService? = null

    @Autowired
    private val dealDamageToPlayerService: DealDamageToPlayerService? = null

    @Autowired
    private val dealDamageToCreatureService: DealDamageToCreatureService? = null

    @Autowired
    private val lifeService: LifeService? = null

    @Autowired
    private val whenTriggerService: WhenTriggerService? = null

    @AfterEach
    fun cleanup() {
        Mockito.reset<Any?>(lifeService, dealDamageToCreatureService, dealDamageToPlayerService)
    }

    @Test
    fun combatShouldWorkIfNoAttackingCreatures() {
        combatService!!.dealCombatDamage(testUtils!!.testGameStatus())
    }

    @Test
    fun unblockedCreatureDealsDamageToPlayer() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Feral Maaka"), "player", "player")
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<DealDamageToPlayerService?>(dealDamageToPlayerService)
            .dealDamageToPlayer(gameStatus, 2, gameStatus.getNonCurrentPlayer())
    }

    @Test
    fun lifelinkCreatureGainsLifeWhenDealingDamage() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature1 =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        val attackingCreature2 =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature1)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature2)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1, 2))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(3, mutableListOf<Int?>(1)))
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<LifeService?>(lifeService).add(gameStatus.getCurrentPlayer(), 2, gameStatus)
    }

    @Test
    fun trampleCreatureDealsRemainingDamageToPlayer() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Charging Monstrosaur"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(2, mutableListOf<Int?>(1)))
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, blockingCreature, 2, false, attackingCreature)
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, attackingCreature, 2, false, blockingCreature)
        Mockito.verify<DealDamageToPlayerService?>(dealDamageToPlayerService)
            .dealDamageToPlayer(gameStatus, 3, gameStatus.getNonCurrentPlayer())
    }

    @Test
    fun deathtouchDamageToCreature() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(2, mutableListOf<Int?>(1)))
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, blockingCreature, 1, true, attackingCreature)
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, attackingCreature, 2, false, blockingCreature)
    }

    @Test
    fun lifelinkNotHappeningIfBlockedCreatureIsReturnedToHandAndNoDamageIsDealt() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(2, mutableListOf<Int?>(1)))
        gameStatus.extractCardByIdFromAnyBattlefield(2)
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verifyNoInteractions(lifeService)
        Mockito.verifyNoInteractions(dealDamageToPlayerService)
        Mockito.verifyNoInteractions(dealDamageToCreatureService)
    }

    @Test
    fun onlyFirstStrikeAndDoubleStrikeCreaturesDealDamageDuringFirstStrike() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature1 =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Fencing Ace"), PLAYER, PLAYER) // 1/1 double strike
        val attackingCreature2 = cardInstanceFactory.create(
            gameStatus,
            2,
            cards.get("Youthful Knight"),
            PLAYER,
            PLAYER
        ) // 2/1 first strike
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature1)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature2)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT) // 2/2
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1, 2))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(3, mutableListOf<Int?>(1)))
        gameStatus.turn.setCurrentPhase(FirstStrikePhase.FS)
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, blockingCreature, 1, false, attackingCreature1)
        Mockito.verifyNoMoreInteractions(dealDamageToCreatureService)
        Mockito.verify<DealDamageToPlayerService?>(dealDamageToPlayerService)
            .dealDamageToPlayer(gameStatus, 2, gameStatus.getNonCurrentPlayer())
    }

    @Test
    fun firstStrikeCreaturesDoNotDealDamageDuringCombat() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature1 = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Fencing Ace"), PLAYER, PLAYER)
        val attackingCreature2 =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Youthful Knight"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature1)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature2)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1, 2))
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(3, mutableListOf<Int?>(1)))
        combatService!!.dealCombatDamage(gameStatus)

        // Then
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, blockingCreature, 1, false, attackingCreature1)
        Mockito.verify<DealDamageToCreatureService?>(dealDamageToCreatureService)
            .dealDamageToCreature(gameStatus, attackingCreature1, 2, false, blockingCreature)
        Mockito.verifyNoMoreInteractions(dealDamageToCreatureService)
        Mockito.verifyNoInteractions(dealDamageToPlayerService)
    }

    @Test
    fun declareAttackerTrigger() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val attackingCreature =
            cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Brazen Wolves"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))

        // Then
        Mockito.verify<WhenTriggerService?>(whenTriggerService)
            .whenTriggered(gameStatus, attackingCreature, TriggerSubtype.WHEN_ATTACK)
    }

    @Test
    fun declareBlockerTrigger() {
        // Given a creature is attacking
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.turn.setCurrentPhase(DeclareAttackersPhase.DA)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_ATTACKERS)
        val attackingCreature = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Feral Maaka"), PLAYER, PLAYER)
        gameStatus.getCurrentPlayer().battlefield.addCard(attackingCreature)
        declareAttackerService!!.declareAttackers(gameStatus, mutableListOf<Int?>(1))

        // And a creature with a when block trigger blocks it
        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Hamlet Captain"), OPPONENT, OPPONENT)
        gameStatus.getNonCurrentPlayer().battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.setCurrentPhase(DeclareBlockersPhase.DB)
        gameStatus.turn.setInputRequiredAction(InputRequiredActions.DECLARE_BLOCKERS)
        declareBlockerService!!.declareBlockers(gameStatus, Map.of<Int?, MutableList<Int?>?>(2, mutableListOf<Int?>(1)))

        // Then
        Mockito.verify<WhenTriggerService?>(whenTriggerService)
            .whenTriggered(gameStatus, blockingCreature, TriggerSubtype.WHEN_BLOCK)
    }

    companion object {
        private const val PLAYER = "player-name"
        const val OPPONENT: String = "opponent-name"
    }
}