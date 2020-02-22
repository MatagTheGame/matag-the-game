package integration.mtg.game.turn.action.life;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.life.AddXLifeAction;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LifeTestConfiguration.class)
public class AddXLifeActionTest {

    @Autowired
    private AddXLifeAction addXLifeAction;

    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private Cards cards;

    @Test
    public void addLifeToPlayer() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Lich's Caress"), "player-name", "player-name");

        // When
        Ability gain3Life = Ability.builder().abilityType(AbilityType.ADD_X_LIFE).parameters(singletonList("3")).build();
        addXLifeAction.perform(cardInstance, gameStatus, new CardInstanceAbility(gain3Life));

        // Then
        assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(23);
    }
}