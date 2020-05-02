package integration.cardinstance.ability;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestUtilsConfiguration.class)
public class MenaceAbilityTest {

    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private Cards cards;

    @Autowired
    private TestUtils testUtils;

    @Test
    public void shouldBlockWhenItHasTwoOrMoreBlockers() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        // When
        CardInstance boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
        CardInstance firstAirElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");
        CardInstance secondAirElemental = cardInstanceFactory.create(gameStatus, 3, cards.get("Air Elemental"), "opponent");

        // Then
        firstAirElemental.checkIfCanBlock(boggartBrute, Lists.newArrayList(firstAirElemental, secondAirElemental));
    }

    @Test(expected = MessageException.class)
    public void shouldNotBlockWhenItHasOneBlocker() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        // When
        CardInstance boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
        CardInstance airElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");

        // Then
        airElemental.checkIfCanBlock(boggartBrute, Lists.newArrayList(airElemental));
    }

}
