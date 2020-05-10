package integration.turn.action.life;

import com.matag.cards.Cards;
import com.matag.cards.ability.Ability;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.life.AddXLifeAction;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    Ability youGain3Life = cardInstance.getAbilities().get(1);
    addXLifeAction.perform(cardInstance, gameStatus, new CardInstanceAbility(youGain3Life));

    // Then
    assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(23);
  }

  @Test
  public void addLifeToAllPlayers() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Centaur Peacemaker"), "player-name", "player-name");

    // When
    Ability eachPlayersGains4Life = cardInstance.getAbilities().get(0);
    addXLifeAction.perform(cardInstance, gameStatus, new CardInstanceAbility(eachPlayersGains4Life));

    // Then
    assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(24);
    assertThat(gameStatus.getPlayerByName("opponent-name").getLife()).isEqualTo(24);
  }

  @Test
  public void subtractLifeToAllOpponents() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Poison-Tip Archer"), "player-name", "player-name");

    // When
    Ability eachPlayersGains4Life = cardInstance.getAbilities().get(2);
    addXLifeAction.perform(cardInstance, gameStatus, new CardInstanceAbility(eachPlayersGains4Life));

    // Then
    assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(20);
    assertThat(gameStatus.getPlayerByName("opponent-name").getLife()).isEqualTo(19);
  }
}