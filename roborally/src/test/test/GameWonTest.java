import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

class GameWonTest {
    private final int TEST_WIDTH = 4;
    private final int TEST_HEIGHT = 4;

    private GameController gameController;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(0, 0));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Test to check if the player can win
     *
     * @author Christoffer Fink s205499
     * @author Phillip  s224278
     */
    @Test
    void gameWon() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        board.getSpace(0, 0).setPlayer(player1);
        board.setTotalCheckpoints(1);

        board.getCurrentPlayer().setCheckpoints(1);

        Assertions.assertTrue(board.isWon(), "The game should be won!");
    }
}
