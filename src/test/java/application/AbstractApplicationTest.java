package application;

import application.browser.MtgBrowser;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.web.server.LocalServerPort;

import static com.aa.mtg.game.player.PlayerType.PLAYER;

public class AbstractApplicationTest {

    @LocalServerPort
    private int port;

    protected MtgBrowser browser;

    @Before
    public void setup() {
        // When player1 joins the game is waiting for opponent
        browser = new MtgBrowser(port);
        browser.getMessageHelper().hasMessage("Waiting for opponent...");

        // When player2 joins the game both players see the table with the cards
        browser.openSecondTab();

        // Make sure player1 is Pippo and player2 is Pluto
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveName();
        browser.player2().getPlayerInfoHelper(PLAYER).toHaveName();
        if (browser.player1().getPlayerInfoHelper(PLAYER).getPlayerName().equals("Pluto")) {
            browser.swapTabs();
        }
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveName("Pippo");
        browser.player2().getPlayerInfoHelper(PLAYER).toHaveName("Pluto");

        // Message disappears
        browser.player1().getMessageHelper().hasNoMessage();
        browser.player2().getMessageHelper().hasNoMessage();

        // Status is
        browser.player1().getStatusHelper().hasMessage("Play any spell or abilities or continue (SPACE).");
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    }

    @After
    public void cleanup() {
        browser.close();
    }
}
