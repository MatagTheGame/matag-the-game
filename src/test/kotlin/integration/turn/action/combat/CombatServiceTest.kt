package integration.turn.action.combat

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.InputRequiredActions
import com.matag.game.turn.action.combat.CombatService
import com.matag.game.turn.action.combat.DeclareAttackerService
import com.matag.game.turn.action.combat.DeclareBlockerService
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.combat.FirstStrikePhase
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class CombatServiceTest(
    private val cardInstanceFactory: CardInstanceFactory,
    private val testUtils: TestUtils,
    private val cards: Cards,
    private val combatService: CombatService,
    private val declareAttackerService: DeclareAttackerService,
    private val declareBlockerService: DeclareBlockerService
) {

    @Test
    fun `combat should work if no attacking creatures`() {
        combatService.dealCombatDamage(testUtils.testGameStatus())
    }

    @Test
    fun `unblocked creature deals damage to player`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player", "player")
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1))
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(gameStatus.nonCurrentPlayer.life).isEqualTo(18)
    }

    @Test
    fun `lifelink creature gains life when dealing damage`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature1 =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        val attackingCreature2 =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature1)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature2)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1, 2))
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(3 to listOf(1)))
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(gameStatus.currentPlayer.life).isEqualTo(22)
    }

    @Test
    fun `trample creature deals remaining damage to player`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Charging Monstrosaur"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1))
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(2 to listOf(1)))
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(blockingCreature.modifiers.damage).isEqualTo(2)
        assertThat(attackingCreature.modifiers.damage).isEqualTo(2)
        assertThat(gameStatus.nonCurrentPlayer.life).isEqualTo(17)
    }

    @Test
    fun `deathtouch damage to creature`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1))
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(2 to listOf(1)))
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(blockingCreature.modifiers.damage).isEqualTo(1)
        assertThat(blockingCreature.modifiers.modifiersUntilEndOfTurn.isToBeDestroyed).isTrue
        assertThat(attackingCreature.modifiers.damage).isEqualTo(2)
    }

    @Test
    fun `lifelink not happening if blocked creature is returned to hand and no damage is dealt`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        val blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT)
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)
        // Add Unsummon to player so autocontinue is not triggered
        gameStatus.nonCurrentPlayer.battlefield.addCard(cardInstanceFactory.create(gameStatus, 3, cards.get("Island"), OPPONENT, OPPONENT))
        gameStatus.nonCurrentPlayer.hand.addCard(cardInstanceFactory.create(gameStatus, 4, cards.get("Unsummon"), OPPONENT, OPPONENT))

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1))
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(2 to listOf(1)))
        gameStatus.extractCardByIdFromAnyBattlefield(2) // play Unsummon
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(gameStatus.currentPlayer.life).isEqualTo(20)
    }

    @Test
    fun `only first strike and double strike creatures deal damage during first strike phase`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Fencing Ace"), PLAYER, PLAYER) // 1/1 double strike
        val attackingCreature2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Youthful Knight"), PLAYER, PLAYER) // 2/1 first strike
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature1)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature2)

        val blockingCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT) // 2/2
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1, 2))
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(3 to listOf(2)))
        gameStatus.turn.currentPhase = FirstStrikePhase.FS
        combatService.dealCombatDamage(gameStatus)

        // Then
        assertThat(attackingCreature2.modifiers.damage).isEqualTo(0)
        assertThat(gameStatus.nonCurrentPlayer.battlefield.cards).isEmpty()
        assertThat(gameStatus.nonCurrentPlayer.life).isEqualTo(18)
    }

    @Test
    fun `declare attacker trigger`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Brazen Wolves"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        declareAttackerService.declareAttackers(gameStatus, listOf(1))

        // Then
        assertThat(gameStatus.stack.items).contains(attackingCreature)
    }

    @Test
    fun `declare blocker trigger`() {
        // Given a creature is attacking
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentPhase = DeclareAttackersPhase.DA
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_ATTACKERS
        val attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), PLAYER, PLAYER)
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)
        declareAttackerService.declareAttackers(gameStatus, listOf(1))

        // And a creature with a when block trigger blocks it
        val blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Hamlet Captain"), OPPONENT, OPPONENT)
        gameStatus.nonCurrentPlayer.battlefield.addCard(blockingCreature)

        // When
        gameStatus.turn.currentPhase = DeclareBlockersPhase.DB
        gameStatus.turn.inputRequiredAction = InputRequiredActions.DECLARE_BLOCKERS
        declareBlockerService.declareBlockers(gameStatus, mapOf(2 to listOf(1)))

        // Then
        assertThat(gameStatus.stack.items).contains(blockingCreature)
    }

    companion object {
        private const val PLAYER = "player-name"
        const val OPPONENT: String = "opponent-name"
    }
}