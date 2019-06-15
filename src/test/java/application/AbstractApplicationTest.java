package application;

import application.browser.MtgBrowser;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.web.server.LocalServerPort;

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

        // Message disappears
        browser.player1().getMessageHelper().hasNoMessage();
        browser.player2().getMessageHelper().hasNoMessage();

        // Status is
        browser.player1().getStatusHelper().hasMessage("Play any spell or abilities or continue (SPACE).");
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    }

    @After
    public void cleanup() {
        browser.player2().close();
        browser.player1().close();
    }
}
