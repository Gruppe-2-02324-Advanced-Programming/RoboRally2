import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBeltCorner;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Gears;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

public class ConveyorBeltTest {

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
     * Test to check if the conveyor belt moves the player forward
     *
     * @author Christoffer Fink s205499
     */
    @Test
    void ConveyorBelt1forwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        // Set up the conveyor belt action on space (0, 1) to move the player north
        board.getSpace(0, 1).addAction(new ConveyorBelt(Heading.NORTH));

        // Move the player to space (0, 1)
        gameController.moveForward(current); // Assuming this moves to space (0, 1)

        // Execute the conveyor belt action
        board.getSpace(0, 1).getActions().get(0).doAction(gameController, board.getSpace(0, 1));

        // Check that the player has moved to space (0, 0)
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should be at Space (0, 0)!");
    }


@Test
void ConveyorBeltCornerRightTest() {
    Board board = gameController.board;
    Player current = board.getCurrentPlayer();

    // Set up the conveyor belt corner action on space (0, 1) to turn right and move the player east
    board.getSpace(1, 1).addAction(new ConveyorBeltCorner(Heading.EAST));
    // Turn the player heading to EAST
    current.setHeading(Heading.EAST);
    // Move the player to space (1, 0)
    gameController.moveForward(current); // Assuming this moves to space (1, 0)
    // Turn the player heading to SOUTH
    current.setHeading(Heading.SOUTH);
    // Move the player to space (1, 1)
    gameController.moveForward(current); // Assuming this moves to space (0, 1)
    // Execute the conveyor belt corner action
    board.getSpace(1, 1).getActions().get(0).doAction(gameController, board.getSpace(1, 1));


    Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player should be heading South!");
    Assertions.assertEquals(current, board.getSpace(2, 1).getPlayer(), "Player should be at Space (2, 1)!");
    }
}


