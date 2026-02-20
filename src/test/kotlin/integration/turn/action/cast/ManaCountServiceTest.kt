package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.cards.properties.Cost
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.ManaCountService
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
class ManaCountServiceTest(
    private val manaCountService: ManaCountService,
    private val cardInstanceFactory: CardInstanceFactory,
    private val testUtils: TestUtils,
    private val cards: Cards
) {
    //  @Rule
    //  public ExpectedException thrown = ExpectedException.none();
    
    @Test
    fun countManaPaidForSimpleLands() {
        // Given
        val mana = mapOf(
            1 to listOf("WHITE"),
            2 to listOf("WHITE"),
            3 to listOf("BLUE")
        )
        val gameStatus = testUtils.testGameStatus()
        val player = gameStatus.player1!!

        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                1,
                cards.get("Plains"),
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
        val colors = manaCountService.verifyManaPaid(mana, player)

        // Then
        assertThat(colors).containsExactlyInAnyOrder(Cost.WHITE, Cost.WHITE, Cost.BLUE)
    }

    //  @Test
    //  public void countManaPaidTappingInstant() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("WHITE")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.player1;
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
    //    var player = gameStatus.player1;
    //
    //    var plains = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.name, player.name);
    //    plains.modifiers.isTapped = true;
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
    //    var player = gameStatus.player1;
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
        val mana = mapOf(
            1 to listOf("BLUE")
        )
        val gameStatus = testUtils.testGameStatus()
        val player = gameStatus.player1!!

        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                1,
                cards.get("Azorius Guildgate"),
                player.name,
                player.name
            )
        )

        // When
        val colors = manaCountService.verifyManaPaid(mana, player)

        // Then
        assertThat(colors).isEqualTo(listOf(Cost.BLUE))
    }

    //  @Test
    //  public void countManaPaidTappingLandForDualLandError() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("BLACK")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.player1;
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
        val mana = mapOf(
            1 to listOf("WHITE"),
            2 to listOf("COLORLESS")
        )
        val gameStatus = testUtils.testGameStatus()
        val player = gameStatus.player1!!

        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                1,
                cards.get("Plains"),
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
        manaCountService.verifyManaPaid(mana, player)
    }

    @Test
    fun countManaPaidTappingCreatureWhichGeneratesTwoMana() {
        // Given
        val mana = mapOf(
            1 to listOf("GREEN", "BLUE")
        )
        val gameStatus = testUtils.testGameStatus()
        val player = gameStatus.player1!!

        player.battlefield.addCard(
            cardInstanceFactory.create(
                gameStatus,
                1,
                cards.get("Gyre Engineer"),
                player.name,
                player.name
            )
        )

        // When
        manaCountService.verifyManaPaid(mana, player)
    }

//  @Test
    //  public void countManaPaidTappingCreatureWhichGeneratesTwoManaException() {
    //    // Given
    //    var mana = Map.of(
    //      1, List.of("GREEN", "BLACK")
    //    );
    //    var gameStatus = testUtils.testGameStatus();
    //    var player = gameStatus.player1;
    //
    //    player.battlefield.addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Gyre Engineer"), player.name, player.name));
    //
    //    thrown.expectMessage("\"1 - Gyre Engineer\" cannot produce BLACK");
    //
    //    // When
    //    manaCountService.verifyManaPaid(mana, player);
    //  }
}