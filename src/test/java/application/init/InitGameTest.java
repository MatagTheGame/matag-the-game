package application.init;

import application.MtgBrowser;
import com.aa.mtg.MtgApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InitGameTest {
    @LocalServerPort
    private int port;

    @Test
    public void display() {
        // When player1 joins the game is waiting for opponent
        MtgBrowser player1 = new MtgBrowser(port);
        player1.displaysMessage("Waiting for opponent...");

        // When player2 joins the game both player see the table
        MtgBrowser player2 = new MtgBrowser(port);
        player1.hasNoMessage();
        player2.hasNoMessage();

        player1.close();
        player2.close();
    }
}
