package com.matag.cards;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.matag.cards.properties.Color.RED;
import static com.matag.cards.properties.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class CardUtilsTest {

  @Autowired
  private Cards cards;

  @Test
  public void isColorless() {
    assertThat(CardUtils.isColorless(cards.get("Bishop's Soldier"))).isFalse();
    assertThat(CardUtils.isColorless(cards.get("Jousting Dummy"))).isTrue();
  }

  @Test
  public void isOnlyColorless() {
    assertThat(CardUtils.isOfOnlyAnyOfTheColors(cards.get("Inspiring Captain"), Set.of(WHITE, RED))).isTrue();
    assertThat(CardUtils.isOfOnlyAnyOfTheColors(cards.get("Fiery Finish"), Set.of(WHITE, RED))).isTrue();
    assertThat(CardUtils.isOfOnlyAnyOfTheColors(cards.get("Inspiring Veteran"), Set.of(WHITE, RED))).isTrue();
    assertThat(CardUtils.isOfOnlyAnyOfTheColors(cards.get("Centaur Peacemaker"), Set.of(WHITE, RED))).isFalse();
    assertThat(CardUtils.isOfOnlyAnyOfTheColors(cards.get("Jousting Dummy"), Set.of(WHITE, RED))).isFalse();
  }
}