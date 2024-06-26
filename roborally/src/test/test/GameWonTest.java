import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void GameWin() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        board.getSpace(0, 0).setPlayer(player1);
        board.setTotalCheckpoints(2);

        Checkpoint checkpoint1 = new Checkpoint(1);
        Checkpoint checkpoint2 = new Checkpoint(2);

        // Add checkpoints to a list
        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(checkpoint1);
        checkpoints.add(checkpoint2);

        // Loop through the checkpoints
        for (Checkpoint checkpoint : checkpoints) {
            // Player lands on the checkpoint
            assertTrue(checkpoint.doAction(gameController, board.getSpace(0, 0)));
        }

        // Check if the game is won
        assertTrue(board.isWon(), "The game should be won!");
    }

    @Test
    void GameWinCheckpointOrder() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        board.getSpace(0, 0).setPlayer(player1);
        board.setTotalCheckpoints(2);


        //we just swap the order of the checkpoints to test if the checkpoints need to be reached in order
        Checkpoint checkpoint1 = new Checkpoint(2);
        Checkpoint checkpoint2 = new Checkpoint(1);


        // Add checkpoints to a list
        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(checkpoint1);
        checkpoints.add(checkpoint2);

        // Loop through the checkpoints
        for (Checkpoint checkpoint : checkpoints) {
            // Player lands on the checkpoint
            assertTrue(checkpoint.doAction(gameController, board.getSpace(0, 0)));
        }

        // Check if the game is won
        assertFalse(board.isWon(), "The game not should be won!");
    }

}
