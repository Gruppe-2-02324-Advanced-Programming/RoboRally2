import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Gears;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

class GearTest {
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
     * 1. Test to check if left-gear turns player
     * @author Christoffer Fink s205499
     *
     */
    @Test
    void moveForwardLeftGear() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).addAction(new Gears(Gears.LEFT_TURN));
        gameController.moveForward(current);
        board.getSpace(0,1).getActions().get(0).doAction(gameController, board.getSpace(0,1));
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * 2. Test to check if right-gear turns player
     * @author Christoffer Fink 205449
     *
     */
    @Test
    void moveForwardRightGear() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).addAction(new Gears(Gears.RIGHT_TURN));
        gameController.moveForward(current);
        board.getSpace(0,1).getActions().get(0).doAction(gameController, board.getSpace(0,1));
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }
}
