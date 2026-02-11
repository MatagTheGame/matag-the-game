package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.cards.properties.Cost
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.ManaCountService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Map

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CastTestConfiguration::class])
class ManaCountServiceTest {
    @Autowired
    private val manaCountService: ManaCountService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    //  @Rule
    //  public ExpectedException thrown = ExpectedException.none();
    @Test
    fun countManaPaidForSimpleLands() {
        // Given
        val mana: MutableMap<Int, List<String>> = Map.of<Int, List<String>>(
            1, listOf("WHITE"),
            2, listOf("WHITE"),
            3, listOf("BLUE")
        )
        val gameStatus = testUtils!!.testGameStatus()
        val player = gameStatus.getPlayer1()

        player.battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                1,
                cards!!.get("Plains"),
                player.name,
                player.name
            )
        )
        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                2,
                cards.get("Plains"),
                player.name,
                player.name
            )
        )
        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                3,
                cards.get("Island"),
                player.name,
                player.name
            )
        )

        // When
        val colors = manaCountService!!.verifyManaPaid(mana, player)

        // Then
        Assertions.assertThat<Cost?>(colors).containsExactlyInAnyOrder(Cost.WHITE, Cost.WHITE, Cost.BLUE)
    }

    //  @Test
    //  public void countManaPaidTappingInstant() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("WHITE")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.getPlayer1();
    //
    //    player.battlefield.addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), player.name, player.name));
    //
    //    thrown.expectMessage("\"1 - Dark Remedy\" cannot be tapped for mana.");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
    //  @Test
    //  public void countManaPaidTappingAlreadyTappedLand() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("WHITE")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.getPlayer1();
    //
    //    var plains = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.name, player.name);
    //    plains.modifiers.setTapped(true);
    //    player.battlefield.addCard(plains);
    //
    //    thrown.expectMessage("\"1 - Plains\" is already tapped.");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
    //  @Test
    //  public void countManaPaidTappingLandForWrongColor() {
    //    // Given
    //    var mana = Map.of(1, List.of("BLUE"));
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.getPlayer1();
    //
    //    player.battlefield.addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.name, player.name));
    //
    //    thrown.expectMessage("\"1 - Plains\" cannot produce BLUE");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
    @Test
    fun countManaPaidTappingLandForDualLand() {
        // Given
        val mana: MutableMap<Int?, List<String>> = Map.of<Int?, List<String>>(
            1, listOf("BLUE")
        )
        val gameStatus = testUtils!!.testGameStatus()
        val player = gameStatus.getPlayer1()

        player.battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                1,
                cards!!.get("Azorius Guildgate"),
                player.name,
                player.name
            )
        )

        // When
        val colors = manaCountService!!.verifyManaPaid(mana, player)

        // Then
        Assertions.assertThat<Cost?>(colors).isEqualTo(listOf(Cost.BLUE))
    }

    //  @Test
    //  public void countManaPaidTappingLandForDualLandError() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("BLACK")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.getPlayer1();
    //
    //    player.battlefield.addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Azorius Guildgate"), player.name, player.name));
    //
    //    thrown.expectMessage("\"1 - Azorius Guildgate\" cannot produce BLACK");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
    @Test
    fun countManaPaidColorlessMana() {
        // Given
        val mana: MutableMap<Int?, List<String>> = Map.of<Int?, List<String>>(
            1, listOf("WHITE"),
            2, listOf("COLORLESS")
        )
        val gameStatus = testUtils!!.testGameStatus()
        val player = gameStatus.getPlayer1()

        player.battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                1,
                cards!!.get("Plains"),
                player.name,
                player.name
            )
        )
        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                2,
                cards.get("Sunscorched Desert"),
                player.name,
                player.name
            )
        )

        // When
        manaCountService!!.verifyManaPaid(mana, player)
    }

    @Test
    fun countManaPaidTappingCreatureWhichGeneratesTwoMana() {
        // Given
        val mana: MutableMap<Int?, List<String>> = Map.of<Int?, List<String>>(
            1, listOf("GREEN", "BLUE")
        )
        val gameStatus = testUtils!!.testGameStatus()
        val player = gameStatus.getPlayer1()

        player.battlefield.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                1,
                cards!!.get("Gyre Engineer"),
                player.name,
                player.name
            )
        )

        // When
        manaCountService!!.verifyManaPaid(mana, player)
    } //  @Test
    //  public void countManaPaidTappingCreatureWhichGeneratesTwoManaException() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("GREEN", "BLACK")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.getPlayer1();
    //
    //    player.battlefield.addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Gyre Engineer"), player.name, player.name));
    //
    //    thrown.expectMessage("\"1 - Gyre Engineer\" cannot produce BLACK");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
}