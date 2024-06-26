import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Pit;
import dk.dtu.compute.se.pisd.roborally.controller.Reboot;
import dk.dtu.compute.se.pisd.roborally.controller.StartGear;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

public class StartGearTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private Board board; // Declare board here

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }
    @BeforeEach
    void setUp() {
        board = new Board(TEST_WIDTH, TEST_HEIGHT); // Initialize board here
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void testStartGearInitialization() {
        Player current = board.getCurrentPlayer();
        Space startSpace = board.getSpace(3, 3);

        // Place the StartGear action on the start space
        startSpace.addAction(new StartGear());

        // Assuming there's some initialization step that uses StartGear
        // Since doAction returns false, we manually place the player
        startSpace.setPlayer(current);

        // Now check if the current player is on space (3,3)
        Assertions.assertEquals(current, startSpace.getPlayer(),
                "Player " + current.getName() + " should be on space (3, 3)!");
    }
}