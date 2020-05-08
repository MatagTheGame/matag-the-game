package integration.turn.action.combat;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.action.combat.DeclareAttackerService;
import com.matag.game.turn.action.combat.DeclareBlockerService;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.life.LifeService;
import integration.TestUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.FirstStrikePhase.FS;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CombatTestConfiguration.class)
public class CombatServiceTest {
  private static final String PLAYER = "player-name";
  public static final String OPPONENT = "opponent-name";

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Autowired
  private CombatService combatService;

  @Autowired
  private DeclareAttackerService declareAttackerService;

  @Autowired
  private DeclareBlockerService declareBlockerService;

  @Autowired
  private DealDamageToPlayerService dealDamageToPlayerService;

  @Autowired
  private DealDamageToCreatureService dealDamageToCreatureService;

  @Autowired
  private LifeService lifeService;

  @After
  public void cleanup() {
    Mockito.reset(lifeService, dealDamageToCreatureService, dealDamageToPlayerService);
  }

  @Test
  public void combatShouldWorkIfNoAttackingCreatures() {
    combatService.dealCombatDamage(testUtils.testGameStatus());
  }

  @Test
  public void unblockedCreatureDealsDamageToPlayer() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player", "player");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1));
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(dealDamageToPlayerService).dealDamageToPlayer(gameStatus, 2, gameStatus.getNonCurrentPlayer());
  }

  @Test
  public void lifelinkCreatureGainsLifeWhenDealingDamage() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER);
    CardInstance attackingCreature2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature1);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature2);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT);
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1, 2));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(3, List.of(1)));
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(lifeService).add(gameStatus.getCurrentPlayer(), 2, gameStatus);
  }

  @Test
  public void trampleCreatureDealsRemainingDamageToPlayer() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Charging Monstrosaur"), PLAYER, PLAYER);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT);
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(2, List.of(1)));
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, blockingCreature, 2, false, attackingCreature);
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, attackingCreature, 2, false, blockingCreature);
    verify(dealDamageToPlayerService).dealDamageToPlayer(gameStatus, 3, gameStatus.getNonCurrentPlayer());
  }

  @Test
  public void deathtouchDamageToCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT);
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(2, List.of(1)));
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, blockingCreature, 1, true, attackingCreature);
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, attackingCreature, 2, false, blockingCreature);
  }

  @Test
  public void lifelinkNotHappeningIfBlockedCreatureIsReturnedToHandAndNoDamageIsDealt() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Vampire of the Dire Moon"), PLAYER, PLAYER);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Feral Maaka"), OPPONENT, OPPONENT);
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(2, List.of(1)));
    gameStatus.extractCardByIdFromAnyBattlefield(2);
    combatService.dealCombatDamage(gameStatus);

    // Then
    verifyNoInteractions(lifeService);
    verifyNoInteractions(dealDamageToPlayerService);
    verifyNoInteractions(dealDamageToCreatureService);
  }

  @Test
  public void onlyFirstStrikeAndDoubleStrikeCreaturesDealDamageDuringFirstStrike() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Fencing Ace"), PLAYER, PLAYER); // 1/1 double strike
    CardInstance attackingCreature2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Youthful Knight"), PLAYER, PLAYER); // 2/1 first strike
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature1);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature2);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT); // 2/2
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1, 2));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(3, List.of(1)));
    gameStatus.getTurn().setCurrentPhase(FS);
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, blockingCreature, 1, false, attackingCreature1);
    verifyNoMoreInteractions(dealDamageToCreatureService);
    verify(dealDamageToPlayerService).dealDamageToPlayer(gameStatus, 2, gameStatus.getNonCurrentPlayer());
  }

  @Test
  public void firstStrikeCreaturesDoNotDealDamageDuringCombat() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(DA);
    CardInstance attackingCreature1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Fencing Ace"), PLAYER, PLAYER);
    CardInstance attackingCreature2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Youthful Knight"), PLAYER, PLAYER);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature1);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature2);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Feral Maaka"), OPPONENT, OPPONENT);
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(blockingCreature);

    // When
    gameStatus.getTurn().setCurrentPhase(DA);
    declareAttackerService.declareAttackers(gameStatus, List.of(1, 2));
    gameStatus.getTurn().setCurrentPhase(DB);
    declareBlockerService.declareBlockers(gameStatus, Map.of(3, List.of(1)));
    combatService.dealCombatDamage(gameStatus);

    // Then
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, blockingCreature, 1, false, attackingCreature1);
    verify(dealDamageToCreatureService).dealDamageToCreature(gameStatus, attackingCreature1, 2, false, blockingCreature);
    verifyNoMoreInteractions(dealDamageToCreatureService);
    verifyNoInteractions(dealDamageToPlayerService);
  }
}