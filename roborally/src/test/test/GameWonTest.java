import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

class GameWonTest {
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

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
            player.setSpace(board.getSpace(i, i));
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
     */
    @Test
    void gameWon() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        board.getSpace(0, 1).addAction(new Checkpoint(1));
        board.getSpace(0, 2).addAction(new Checkpoint(2));

        gameController.moveForward(current);
        board.getSpace(0, 1).getActions().get(0).doAction(gameController, board.getSpace(0, 1));
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should be at Space (0,1)!");
        Assertions.assertTrue(current.getCheckpoints() >= 1, "Player should have reached at least 1 checkpoint");

        gameController.moveForward(current);
        board.getSpace(0, 2).getActions().get(0).doAction(gameController, board.getSpace(0, 2));
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0,2)!");
        Assertions.assertEquals(2, current.getCheckpoints(), "Player should have reached 2 checkpoints");

        Assertions.assertTrue(board.isWon(), "The game should be won!");
    }

    @Test
    void checkpointOrder() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        board.getSpace(0, 1).addAction(new Checkpoint(2));
        board.getSpace(0, 2).addAction(new Checkpoint(1));

        gameController.moveForward(current);
        board.getSpace(0, 1).getActions().get(0).doAction(gameController, board.getSpace(0, 1));
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should be at Space (0,1)!");
        Assertions.assertTrue(current.getCheckpoints() >= 0, "Player should have reached at least 1 checkpoint");

        gameController.moveForward(current);
        board.getSpace(0, 2).getActions().get(0).doAction(gameController, board.getSpace(0, 2));
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0,2)!");
        Assertions.assertEquals(2, current.getCheckpoints(), "Player should have reached 0 checkpoints");

        Assertions.assertFalse(board.isWon(), "The game should not be won!");
    }
}

