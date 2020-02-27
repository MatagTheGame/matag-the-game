# Cards

This module contains all the cards available in this game.


## Scripting

If you are brave enough and want to add a missing card, here's some tips.

Cards are in [cards/src/main/resources](src/main/resources/cards).

Are in json format and it's a bit of a guessing game figure out what abilities have been already implemented.
The best thing is to perform a text search on this folder for a similar card.

After adding the card do not forget to add it as well on the related sets.



When a card is added the `imageUrls` can be removed from the json and automatically fetched from the server by running
CardsTests.cardImageLinker() test.



After a card is coded is a very good idea to test it by starting the app in test mode:
 - adding the `-Dspring.profiles.active=test` profile in the maven command or intellij startup command
 - modifying the `ProdInitTestService` class by setting the board as you would like to start your test.

  