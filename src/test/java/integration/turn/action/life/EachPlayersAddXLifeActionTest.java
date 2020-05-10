package integration.turn.action.life;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.Cards;
import com.matag.cards.ability.Ability;
import com.matag.cards.ability.type.AbilityType;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.life.EachPlayersAddXLifeAction;
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
public class EachPlayersAddXLifeActionTest {

  @Autowired
  private EachPlayersAddXLifeAction eachPlayersAddXLifeAction;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void subtractXLifeToEachPlayer() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(1, cards.get("Dark Remedy"), "player-name");

    // When
    Ability lose4Life = Ability.builder().abilityType(AbilityType.ADD_X_LIFE).parameters(singletonList("-4")).build();
    eachPlayersAddXLifeAction.perform(cardInstance, gameStatus, new CardInstanceAbility(lose4Life));

    // Then
    assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(16);
    assertThat(gameStatus.getPlayerByName("opponent-name").getLife()).isEqualTo(16);
  }
}