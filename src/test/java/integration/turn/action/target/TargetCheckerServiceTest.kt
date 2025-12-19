package integration.turn.action.target;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.target.TargetCheckerService;
import integration.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
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
    var gameStatus = testUtils.testGameStatus();
    var sunscorchedDesert = cardInstanceFactory.create(gameStatus, 1, cards.get("Sunscorched Desert"), "player-name");

    // When
    var isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(sunscorchedDesert, gameStatus);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnTrueIfThereIsSomethingToTarget() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");
    var aCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Banehound"), "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(aCreature);

    // When
    var isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnFalseIfThereIsNothingToTarget() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");

    // When
    var isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isFalse();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnFalseIfTargetHasHexproofAndTargetedByOpponent() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");
    var aCreatureWithHexproof = cardInstanceFactory.create(gameStatus, 2, cards.get("Cold-Water Snapper"), "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(aCreatureWithHexproof);

    // When
    var isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isFalse();
  }

  @Test
  public void checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisitesShouldReturnTrueIfTargetHasHexproofAndTargetedByThePlayer() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var darkRemedy = cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), "player-name");
    var aCreatureWithHexproof = cardInstanceFactory.create(gameStatus, 2, cards.get("Cold-Water Snapper"), "player-name");
    gameStatus.getPlayer2().getBattlefield().addCard(aCreatureWithHexproof);

    // When
    var isValid = targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(darkRemedy, gameStatus);

    // Then
    assertThat(isValid).isTrue();
  }
}
