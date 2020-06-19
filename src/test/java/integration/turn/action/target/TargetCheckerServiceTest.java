package integration.turn.action.target;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.target.TargetCheckerService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TargetTestConfiguration.class)
public class TargetCheckerServiceTest {
  @Autowired
  private TargetCheckerService targetCheckerService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnAlwaysTrueForPlayer() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance sunscorchedDesert = cardInstanceFactory.create(gameStatus, 1, cards.get("Sunscorched Desert"), "player-name");

    // When
    boolean isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(sunscorchedDesert, gameStatus);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnTrueIfThereIsSomethingToTarget() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");
    CardInstance aCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Banehound"), "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(aCreature);

    // When
    boolean isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnFalseIfThereIsNothingToTarget() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");

    // When
    boolean isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isFalse();
  }
}
