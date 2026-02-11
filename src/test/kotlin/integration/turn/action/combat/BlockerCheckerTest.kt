package integration.turn.action.combat

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.combat.BlockerChecker
import integration.TestUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.List

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CombatTestConfiguration::class])
class BlockerCheckerTest {
    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val blockerChecker: BlockerChecker? = null

    @Test
    fun shouldBlockWhenItHasTwoOrMoreBlockers() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()

        val boggartBrute = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Boggart Brute"), "player")
        val firstAirElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent")
        val secondAirElemental = cardInstanceFactory.create(gameStatus, 3, cards.get("Air Elemental"), "opponent")

        // When
        blockerChecker!!.checkIfCanBlock(boggartBrute, List.of<CardInstance?>(firstAirElemental, secondAirElemental))

        // Then no exception is thrown
    } //  @Test
    //  public void shouldNotBlockWhenItHasOneBlocker() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //
    //    var boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
    //    var airElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");
    //
    //    // Expect
    //    thrown.expectMessage("\"2 - Air Elemental\" cannot block \"1 - Boggart Brute\" alone as it has menace.");
    //
    //    // When
    //    blockerChecker.checkIfCanBlock(boggartBrute, List.of(airElemental));
    //  }
}
