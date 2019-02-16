package com.aa.mtg.init;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.player.Battlefield;
import com.aa.mtg.player.Hand;
import com.aa.mtg.player.Library;
import com.aa.mtg.player.Player;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitPlayerEvent {
    private Library library;
    private Hand hand;
    private Battlefield battlefield;
    private String name;
    private int life;

    public static InitPlayerEvent create(Player player) {
        Hand hand = new Hand();
        Library library = new Library();
        Battlefield battlefield = new Battlefield();

        for (CardInstance cardInstance : player.getHand().getCards()) {
            hand.getCards().add(new CardInstance(cardInstance));
        }

        for (CardInstance cardInstance : player.getLibrary().getCards()) {
            library.getCards().add(new CardInstance(cardInstance.getId(), Card.hiddenCard()));
        }

        return new InitPlayerEvent(library, hand, battlefield, player.getName(), player.getLife());
    }
}
